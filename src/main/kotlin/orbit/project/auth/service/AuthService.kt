package orbit.project.auth.service

import orbit.project.auth.http.LoginRequest
import orbit.project.auth.jwt.JwtTokenProvider
import orbit.project.auth.utils.UserActivityService
import orbit.project.member.repository.MemberRepository
import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import orbit.project.utils.fail.FailResponse
import orbit.project.utils.success.SuccessResponse
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
    fun authLogin(loginRequest: LoginRequest): Mono<Any> {
        return memberRepository.findByEmail(loginRequest.email)
            .switchIfEmpty(Mono.error(CustomException(ErrorException.MEMBER_NOT_FOUND)))
            .flatMap { findLoginIdInfo ->
                if (passwordEncoder.matches(loginRequest.password, findLoginIdInfo.password)) {
                    val token = jwtTokenProvider.generateToken(findLoginIdInfo)
                    UserActivityService.updateLastLogin(findLoginIdInfo.memberId!!, memberRepository)

                    val successResponse = SuccessResponse(
                        successCode = 200,
                        successResult = true,
                        data = token
                    )
                    Mono.just(successResponse as Any)
                } else {
                    Mono.error(CustomException(ErrorException.INVALID_PASSWORD))
                }
            }
            .onErrorResume { e ->
                val failResponse = when (e) {
                    is CustomException -> FailResponse(
                        FailCode = e.statusCode,
                        FailResult = false,
                        data = e.message ?: "Unknown error occurred."
                    )

                    else -> FailResponse(
                        FailCode = ErrorException.SERVER_ERROR.statusCode, // 알 수 없는 서버 오류
                        FailResult = false,
                        data = ErrorException.SERVER_ERROR.message ?: "Unknown error occurred."
                    )
                }
                Mono.just(failResponse as Any)
            }
    }
}

