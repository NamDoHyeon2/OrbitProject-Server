package orbit.project.member.http


data class MemberRequest (
    val memberLoginId: String,
    val memberPassword: String,
    val memberName : String,
    val memberEmail: String,
    val memberAuthType : String
){

}