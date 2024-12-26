package orbit.project.member.models

import orbit.project.member.http.MemberRequest
import orbit.project.utils.TimestampDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneId

@Table("member") // 실제 DB 테이블 이름
data class MemberEntity (
    @Id
    val memberId: Long? = null, // PK로 설정, 기본값을 null로 설정
    val groupId: Long? = null,  // 기본값을 null로 설정
    val roleId: Long? = null,   // 기본값을 null로 설정
    val email: String,
    val password: String,
    val name: String,
    val birth : String,
    val phoneNumber : String,
    val job : String,
    val authType: String,
    val inviteCode : String,
    var lastLogin: LocalDateTime? = null,

) : TimestampDto(
    LocalDateTime.now(ZoneId.of("UTC")),
    LocalDateTime.now(ZoneId.of("UTC")),
){
    companion object {
        fun fromRequest(memberRequest: MemberRequest) = MemberEntity(
            memberId = null,          // MemberEntity 생성 시 memberId는 null로 설정
            groupId = 1,           // groupId는 아직 알 수 없으므로 1 -> test_group ( 나중에 디폴트로 변경 )
            roleId = 1,            // roleId도 아직 알 수 없으므로 1 -> test_role (나중에 디폴트로 변경 )
            email = memberRequest.memberEmail,
            password = memberRequest.memberPassword,
            name = memberRequest.memberName,
            birth = memberRequest.memberBirth,
            phoneNumber = memberRequest.memberPhoneNumber,
            job = memberRequest.memberJob,
            authType = memberRequest.memberAuthType,
            inviteCode = memberRequest.inviteCode,
            lastLogin = LocalDateTime.now()
        )
    }
}
