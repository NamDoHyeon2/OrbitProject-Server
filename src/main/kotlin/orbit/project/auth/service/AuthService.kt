package orbit.project.auth.service

import orbit.project.auth.http.LoginRequest
import orbit.project.auth.http.LoginTokenResponse
import orbit.project.auth.jwt.JwtTokenProvider
import orbit.project.auth.utils.UserActivityService
import orbit.project.member.repository.MemberRepository
import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,  // PasswordEncoder 타입으로 주입받기
    private val jwtTokenProvider: JwtTokenProvider,
    ) {

    //로그인 검증
    fun authLogin(loginRequest: LoginRequest): Mono<LoginTokenResponse> {
        println(loginRequest)
        val loginId = loginRequest.loginId
        val password = loginRequest.password

        return memberRepository.findByLoginId(loginId)
            .flatMap { findLoginIdInfo ->
                // 비밀번호 일치 여부 확인
                if (passwordEncoder.matches(password, findLoginIdInfo.password)) {
                    val token = jwtTokenProvider.generateToken(findLoginIdInfo)
                    UserActivityService.updateLastLogin(findLoginIdInfo.memberId!! , memberRepository)
                    Mono.just(token)
                } else {
                    Mono.error(CustomException(ErrorException.INVALID_PASSWORD)) // 비밀번호 일치하지 않으면 에러 발생
                }

            }
    }
}