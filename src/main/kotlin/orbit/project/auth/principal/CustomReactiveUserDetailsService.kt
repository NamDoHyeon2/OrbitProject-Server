package orbit.project.auth.principal

import CustomUserDetails
import orbit.project.member.repository.MemberRepository
import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class CustomReactiveUserDetailsService(
    private val memberRepository: MemberRepository // MemberRepository를 주입받음


) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return memberRepository.findByLoginId(username) // Mono<MemberEntity>? 반환
            .switchIfEmpty(Mono.error(CustomException(ErrorException.MEMBER_NOT_FOUND))) // Mono가 비어 있으면 예외 처리
            .map { memberEntity ->
                // 인증 권한 관련
                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                CustomUserDetails(memberEntity, authorities) // CustomUserDetails 객체 생성
            }
    }

}