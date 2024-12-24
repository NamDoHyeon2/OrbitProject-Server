package orbit.project.email.controller

import orbit.project.email.http.SendVerificationCodeRequest
import orbit.project.email.http.VerificationResponse
import orbit.project.email.http.VerifyCodeRequest
import orbit.project.email.service.VerificationService

import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class VerificationController(
    private val verificationService: VerificationService,
) {

    @PostMapping("/SendCode")
    fun sendVerificationCode(@RequestBody request: SendVerificationCodeRequest): Mono<VerificationResponse> {
        return verificationService.sendVerificationEmailCode(request.email)
    }

    @PostMapping("/VerifyCode")
    fun verifyCode(@RequestBody request: VerifyCodeRequest): Mono<VerificationResponse> {
        return verificationService.verifyCode(request.email, request.code)
    }
}