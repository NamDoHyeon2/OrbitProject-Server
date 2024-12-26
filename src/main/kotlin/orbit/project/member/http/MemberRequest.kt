package orbit.project.member.http


data class MemberRequest (
    val memberEmail: String,
    val memberPassword: String,
    val memberName : String,
    val memberBirth: String,
    val memberPhoneNumber : String,
    val memberJob : String,
    val memberAuthType : String,
    val inviteCode : String,

){

}