package orbit.project.project.service

import orbit.project.auth.utils.UserActivityService
import orbit.project.member.http.MemberResponse
import orbit.project.project.http.ProjectRequest
import orbit.project.project.http.ProjectResponse
import orbit.project.project.models.ProjectEntity
import orbit.project.project.models.ProjectMemberEntity
import orbit.project.project.repository.ProjectMemberRepository
import orbit.project.project.repository.ProjectRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val projectMemberRepository: ProjectMemberRepository
) {

    fun saveProject(projectRequest: ProjectRequest): Mono<ProjectResponse> {
        val saveProjectEntity = ProjectEntity.projectRequestConvertProjectEntity(projectRequest)

        // 비동기로 사용자 이메일을 가져오기
        return UserActivityService.getCurrentUserEmail()
            .flatMap { createdBy ->
                saveProjectEntity.createdBy = createdBy //이메일 관리
                projectRepository.save(saveProjectEntity)
                    .flatMap { resultProjectEntity ->
                        saveProjectMember(resultProjectEntity.projectId!!, projectRequest.projectMemberList)
                            .collectList()
                            .map { projectMemberList ->
                                ProjectResponse(
                                    projectId = resultProjectEntity.projectId,
                                    projectName = resultProjectEntity.projectName,
                                    projectDescription = resultProjectEntity.projectDescription,
                                    projectMemberList = projectMemberList
                                )
                            }
                    }
            }
    }





    private fun saveProjectMember(projectId: Long, memberIds: List<Long>): Flux<MemberResponse> {
        val projectMemberEntities = memberIds.map { memberId ->
            ProjectMemberEntity.convertProjectIdMemberIdToProjectMemberEntity(projectId, memberId)
        }

        // 프로젝트 멤버를 비동기적으로 저장한 후, 프로젝트 ID로 멤버 조회
        return projectMemberRepository.saveAll(projectMemberEntities) // 프로젝트 멤버 저장
            .thenMany(  // thenMany로 Flux<MemberEntity>를 반환 //Mono 결과값을 Flux로 반환할떄 사용.. 뭐이러누
                projectMemberRepository.findMembersByProjectId(projectId) // 해당 프로젝트 ID로 멤버 리스트 조회
                    .flatMap { memberEntity ->
                        MemberResponse.fromEntity(Mono.just(memberEntity)) // 각 멤버를 MemberResponse로 변환
                    }
            )
    }





}