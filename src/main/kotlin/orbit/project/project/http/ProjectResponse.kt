package orbit.project.project.http

import orbit.project.member.http.MemberResponse

data class ProjectResponse(
    val projectId : Long,
    val projectName : String,
    val projectDescription : String,
    val projectMemberList : List<MemberResponse>
) {

}