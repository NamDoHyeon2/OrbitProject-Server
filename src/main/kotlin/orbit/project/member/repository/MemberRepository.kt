package orbit.project.member.repository

import orbit.project.member.models.MemberEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface MemberRepository : ReactiveCrudRepository<MemberEntity, Long> {

    fun existsByLoginId(loginId: String): Mono<Boolean> // 데이터베이스 ID와 입력받은 ID 값 중복 확인

    fun findByLoginId(loginId: String): Mono<MemberEntity>

    fun findByMemberId(memberId: Long): Mono<MemberEntity>

}