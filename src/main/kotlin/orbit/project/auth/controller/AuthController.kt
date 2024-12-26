package orbit.project.auth.controller

import orbit.project.auth.http.LoginRequest
import orbit.project.auth.http.LoginTokenResponse
import orbit.project.auth.service.AuthService
import orbit.project.member.http.MemberResponse
import orbit.project.utils.success.SuccessResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Mono<SuccessResponse<LoginTokenResponse>> =
         authService.authLogin(loginRequest)
            .map { memberResponse -> SuccessResponse(code = 200, message = true, data = memberResponse) }





}