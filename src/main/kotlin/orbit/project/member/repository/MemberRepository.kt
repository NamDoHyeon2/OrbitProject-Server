package orbit.project.member.repository

import orbit.project.member.models.MemberEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface MemberRepository : ReactiveCrudRepository<MemberEntity, Long> {

    fun findByEmail(email: String): Mono<MemberEntity>

    // fun existsByLoginId(loginId: String): Mono<Boolean> // 데이터베이스 ID와 입력받은 ID 값 중복 확인

    fun findByMemberId(memberId: Long): Mono<MemberEntity>

}