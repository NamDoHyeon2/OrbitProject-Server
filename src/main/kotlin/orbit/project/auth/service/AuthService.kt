package orbit.project.auth.service

import orbit.project.auth.http.LoginRequest
import orbit.project.auth.jwt.JwtTokenProvider
import orbit.project.auth.utils.UserActivityService
import orbit.project.member.models.MemberEntity
import orbit.project.member.repository.MemberRepository
import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import orbit.project.utils.fail.FailResponse
import orbit.project.utils.success.SuccessResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import org.springframework.web.reactive.function.client.WebClient

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
                        failCode = e.statusCode,
                        failResult = false,
                        data = e.message ?: "Unknown error occurred."
                    )

                    else -> FailResponse(
                        failCode = ErrorException.SERVER_ERROR.statusCode, // 알 수 없는 서버 오류
                        failResult = false,
                        data = ErrorException.SERVER_ERROR.message ?: "Unknown error occurred."
                    )
                }
                Mono.just(failResponse as Any)
            }
    }

    fun googleLogin(token: String): Mono<Any> {
        val webClient = WebClient.builder()
            .baseUrl("https://www.googleapis.com/oauth2/v3/userinfo")
            .build()

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.queryParam("access_token", token).build()
            }
            .retrieve()
            .bodyToMono(Map::class.java)
            .flatMap { userInfo ->
                val email = userInfo["email"] as? String
                val name = userInfo["name"] as? String

                if (email != null && name != null) {
                    // 데이터베이스에서 사용자 조회
                    memberRepository.findByEmail(email)
                        .switchIfEmpty(
                            // 존재하지 않을 경우 새 사용자 저장
                            memberRepository.save(
                                MemberEntity(
                                    email = email,
                                    password = "", // Google 로그인에서는 비밀번호 사용 안함
                                    name = name,
                                    birth = "null", // 근데 애네는 어떻게 불러오지..? api 사용해도 불러올 수가 없는데..
                                    phoneNumber = "null", // 근데 애네는 어떻게 불러오지..? api 사용해도 불러올 수가 없는데..
                                    job = "null", // 애는 웹에서 직접 대입을 받아야 저장가능
                                    authType = "Google",
                                    inviteCode = "null", // 애는 웹에서 직접 대입을 받아야 저장가능
                                )
                            )
                        )
                        .flatMap { memberEntity ->
                            // JWT 토큰 생성
                            val loginTokenResponse = jwtTokenProvider.generateToken(memberEntity)

                            // 성공 응답 반환
                            Mono.just(SuccessResponse(
                                successCode = 200,
                                successResult = true,
                                data = loginTokenResponse
                            ) as Any)
                        }
                } else {
                    Mono.error(CustomException(ErrorException.INVALID_EMAIL))
                }
            }
            .onErrorResume { e ->
                val failResponse = when (e) {
                    is CustomException -> FailResponse(
                        failCode = e.statusCode,
                        failResult = false,
                        data = e.message ?: "Invalid email or user info."
                    )

                    else -> FailResponse(
                        failCode = ErrorException.SERVER_ERROR.statusCode,
                        failResult = false,
                        data = ErrorException.SERVER_ERROR.message ?: "Unknown server error."
                    )
                }
                Mono.just(failResponse as Any)
            }
    }
}

