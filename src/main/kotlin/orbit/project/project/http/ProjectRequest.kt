package orbit.project.project.http

data class ProjectRequest(
    val projectName : String,
    val projectDescription : String,
    val projectMemberList : List<Long> //이메일 보내주기
)