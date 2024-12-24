package orbit.project.email.service

import orbit.project.email.http.VerificationResponse
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

@Service
class VerificationService(
    private val mailSender: JavaMailSender // JavaMailSender 주입
) {
    private val verificationCodes = ConcurrentHashMap<String, String>() // 메모리 내 저장소

    fun sendVerificationEmailCode(email: String): Mono<VerificationResponse> {
        return Mono.fromCallable {
            val code = generateVerificationCode()
            verificationCodes[email] = code
            println("Generated code: $code for email: $email")
            code
        }
            .flatMap { code ->
                sendEmail(email, code)
                    .thenReturn(VerificationResponse(success = true, message = "Verification code sent successfully."))
            }
            .onErrorResume { ex ->
                println("Error occurred: ${ex.message}")
                Mono.just(VerificationResponse(success = false, message = "Failed to send verification code."))
            }
    }

    fun verifyCode(email: String, inputCode: String): Mono<VerificationResponse> {
        val storedCode = verificationCodes[email]
        return if (storedCode != null && storedCode == inputCode) {
            Mono.just(VerificationResponse(success = true, message = "Verification successful."))
        } else {
            Mono.just(VerificationResponse(success = false, message = "Verification failed. Code does not match."))
        }
    }

    private fun generateVerificationCode(): String {
        return (100000..999999).random().toString() // 6자리 숫자 생성
    }

    private fun sendEmail(email: String, code: String): Mono<Void> {
        return Mono.fromCallable {
            try {
                val mimeMessage = mailSender.createMimeMessage()
                val helper = MimeMessageHelper(mimeMessage, "UTF-8")

                helper.setTo(email) // 수신자 설정
                helper.setSubject("Orbit 인증번호를 확인해주세요.") // 제목 설정
                helper.setText("인증번호 : $code") // 본문 설정
                helper.setFrom("Orbit <skaehgus113@naver.com>") // 발신자 설정

                mailSender.send(mimeMessage) // 이메일 전송
                println("Email successfully sent to $email")
            } catch (ex: Exception) {
                println("Error while sending email to $email: ${ex.message}")
                throw ex // 예외를 상위로 전달
            }
        }.then() // Mono<Void> 반환
    }
}