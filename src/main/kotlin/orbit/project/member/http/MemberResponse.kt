package orbit.project.member.http

import orbit.project.member.models.MemberEntity
import reactor.core.publisher.Mono
import java.time.LocalDateTime

data class MemberResponse(
    val loginId: String,
    val password: String,
    val email: String,
    val createAt : LocalDateTime,
    val updateDateTime : LocalDateTime
) {
    companion object {
        // Mono<MemberEntity>를 Mono<MemberResponse>로 변환
        fun fromEntity(memberEntityMono: Mono<MemberEntity>): Mono<MemberResponse> =
            memberEntityMono.map { memberEntity ->
                MemberResponse(
                    loginId = memberEntity.loginId,
                    password = memberEntity.password,
                    email = memberEntity.email,
                    createAt = memberEntity.createdAt,
                    updateDateTime = memberEntity.updatedAt
                )
            }
    }

}