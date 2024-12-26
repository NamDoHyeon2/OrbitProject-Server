package orbit.project.member.http

import orbit.project.member.models.MemberEntity
import reactor.core.publisher.Mono
import java.time.LocalDateTime

data class MemberResponse(
    val email: String,
    val password: String,
    val name: String,
    val birth: String,
    val phoneNumber: String,
    val job: String,
    val authType: String,
    val inviteCode: String,
    val createAt : LocalDateTime,
    val updateDateTime : LocalDateTime
) {
    companion object {
        // Mono<MemberEntity>를 Mono<MemberResponse>로 변환
        fun fromEntity(memberEntityMono: Mono<MemberEntity>): Mono<MemberResponse> =
            memberEntityMono.map { memberEntity ->
                MemberResponse(
                    email = memberEntity.email,
                    password = memberEntity.password,
                    name = memberEntity.name,
                    birth = memberEntity.birth,
                    phoneNumber = memberEntity.phoneNumber,
                    job = memberEntity.job,
                    authType = memberEntity.authType,
                    inviteCode = memberEntity.inviteCode,
                    createAt = memberEntity.createdAt,
                    updateDateTime = memberEntity.updatedAt
                )
            }
    }

}