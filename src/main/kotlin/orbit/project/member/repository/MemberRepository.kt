package orbit.project.member.repository

import orbit.project.member.models.MemberEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface MemberRepository : ReactiveCrudRepository<MemberEntity, Long> {

    fun findByEmail(email: String): Mono<MemberEntity>

    fun findByMemberId(memberId: Long): Mono<MemberEntity>

}