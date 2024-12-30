package orbit.project.project.dto

import orbit.project.member.models.MemberEntity

//프로젝트랑 멤버 정보를 같이 쓸 경우 ( Member 포함 )
data class ProjectWithMembersDto(
    val projectId: Long?,
    val projectName: String?,
    val projectDescription: String?,
    val members: List<MemberEntity> = emptyList()
)
