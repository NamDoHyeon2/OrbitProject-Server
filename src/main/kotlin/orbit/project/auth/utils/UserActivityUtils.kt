package orbit.project.auth.utils

import orbit.project.member.repository.MemberRepository
import java.time.LocalDateTime

object UserActivityService {

    fun updateLastLogin(userId: Long, memberRepository: MemberRepository) {
        memberRepository.findByMemberId(userId)
            .flatMap { currentUser ->
                currentUser.lastLogin = LocalDateTime.now() // 마지막 시간 현재시간으로 설정
                memberRepository.save(currentUser).then() // 업데이트
            }
            .subscribe() // 리액티브 흐름을 구독하여 실행
    }
}
