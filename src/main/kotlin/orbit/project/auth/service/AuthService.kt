package orbit.project.auth.service

import orbit.project.config.kakao.KakaoConfig
import orbit.project.auth.http.LoginRequest
import orbit.project.auth.jwt.JwtTokenProvider
import orbit.project.auth.utils.UserActivityService
import orbit.project.member.http.MemberRequest
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
    private val kakaoConfig: KakaoConfig,
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

    //구글 로그인 검증
    fun googleLogin(token: String): Mono<Any> {
        val webClient = WebClient.builder()
            .baseUrl("https://oauth2.googleapis.com/tokeninfo")
            .build()

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.queryParam("id_token", token).build()
            }
            .retrieve()
            .bodyToMono(Map::class.java)
            .flatMap { userInfo ->
                val email = userInfo["email"] as? String
                val name = userInfo["name"] as? String

                if (email != null && name != null) {
                    val memberRequest = MemberRequest(
                        memberEmail = email,
                        memberPassword = "", // Google 로그인에서는 비밀번호 사용 안함
                        memberName = name,
                        memberBirth = "2001-04-12", // 기본값 제공
                        memberPhoneNumber = "010-5298-4265", // 기본값 제공
                        memberJob = "BackEnd Developer", // 기본값 제공
                        memberAuthType = "Google",
                        inviteCode = "test" // 기본값 제공
                    )

                    // MemberEntity를 fromRequest를 통해 생성
                    val memberEntity = MemberEntity.fromRequest(memberRequest)

                    memberRepository.findByEmail(email)
                        .switchIfEmpty(
                            memberRepository.save(memberEntity)
                        )
                        .flatMap { savedMemberEntity ->
                            val generatedToken = jwtTokenProvider.generateToken(savedMemberEntity)

                            Mono.just(SuccessResponse(
                                successCode = 200,
                                successResult = true,
                                data = generatedToken
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

    fun kakaoLogin(token: String): Mono<Any> {
        val webClient = WebClient.builder()
            .baseUrl(kakaoConfig.apiUrl)
            .build()

        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/v2/user/me")
                    .queryParam("access_token", token)
                    .queryParam("client_id", kakaoConfig.clientId)
                    .queryParam("client_secret", kakaoConfig.clientSecret) // Client Secret 추가
                    .queryParam("code", token)
                    .build()
            }
            .retrieve()
            .bodyToMono(Map::class.java)
            .flatMap { userInfo ->
                val email = userInfo["kakao_account"]?.let { (it as Map<*, *>)["email"] } as? String
                val name = userInfo["properties"]?.let { (it as Map<*, *>)["nickname"] } as? String

                if (email != null && name != null) {
                    val memberRequest = MemberRequest(
                        memberEmail = email,
                        memberPassword = "", // 비밀번호는 사용하지 않음
                        memberName = name,
                        memberBirth = "2001-04-12", // 기본값
                        memberPhoneNumber = "010-0000-0000", // 기본값
                        memberJob = "Default Job", // 기본값
                        memberAuthType = "Kakao",
                        inviteCode = "test" // 기본값
                    )

                    val memberEntity = MemberEntity.fromRequest(memberRequest)

                    memberRepository.findByEmail(email)
                        .switchIfEmpty(
                            memberRepository.save(memberEntity)
                        )
                        .flatMap { savedMemberEntity ->
                            val loginTokenResponse = jwtTokenProvider.generateToken(savedMemberEntity)
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
                        data = e.message ?: "Unknown error occurred."
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

