package orbit.project.project.repository

import orbit.project.member.models.MemberEntity
import orbit.project.project.models.ProjectMemberEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ProjectMemberRepository  : ReactiveCrudRepository<ProjectMemberEntity, Long>{

    @Query("SELECT m.* " +
            "FROM member m " +
            "JOIN project_member pm ON m.member_id = pm.member_id " +
            "WHERE pm.project_id = :projectId")
    fun findMembersByProjectId(projectId: Long): Flux<MemberEntity>

}