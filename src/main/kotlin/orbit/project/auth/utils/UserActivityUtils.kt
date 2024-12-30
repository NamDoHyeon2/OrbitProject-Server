package orbit.project.auth.utils

import CustomUserDetails
import orbit.project.member.repository.MemberRepository
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import reactor.core.publisher.Mono
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

    fun getCurrentUserEmail(): Mono<String?> {
        return ReactiveSecurityContextHolder.getContext()  // Mono<SecurityContext>를 반환
            .mapNotNull { securityContext ->
                val authentication = securityContext.authentication
                val customUserDetails = authentication.principal as? CustomUserDetails
                customUserDetails?.getEmail().toString()  // CustomUserDetails에서 이메일을 가져오기
            }
    }

}
