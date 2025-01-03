package orbit.project.auth.service

import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import orbit.project.utils.fail.FailResponse
import orbit.project.utils.success.SuccessResponse
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

import java.time.LocalDateTime
import java.time.Duration

@Service
class VerificationService(
    private val mailSender: JavaMailSender
) {
    private val verificationCodes = ConcurrentHashMap<String, Pair<String, LocalDateTime>>() // 인증 코드와 생성 시간 저장

    fun sendVerificationEmailCode(email: String): Mono<Any> {
        return Mono.fromCallable {
            val code = generateVerificationCode()
            val createdTime = LocalDateTime.now() // 현재 시간
            verificationCodes[email] = Pair(code, createdTime) // 인증 코드와 생성 시간 저장
            println("Generated code: $code for email: $email at $createdTime")
            code
        }.flatMap { code ->
            sendEmail(email, code)
                .thenReturn(
                    SuccessResponse(
                        successCode = 200,
                        successResult = true,
                        data = code
                    ) as Any
                )
        }.onErrorResume { ex ->
            handleFailure(ex)
        }
    }

    fun verifyCode(email: String, inputCode: String, requestTime: LocalDateTime): Mono<Any> {
        return Mono.fromCallable {
            val (storedCode, createdTime) = verificationCodes[email]
                ?: throw CustomException(ErrorException.EMAIL_CODE_NOT_FOUND)

            val now = LocalDateTime.now() // 서버 현재 시간
            val duration = Duration.between(createdTime, now)

            if (duration.toMinutes() > 5) {
                verificationCodes.remove(email) // 만료된 코드 제거
                throw CustomException(ErrorException.EMAIL_CODE_EXPIRED)
            }

            if (storedCode != inputCode) {
                throw CustomException(ErrorException.INVALID_EMAIL_CODE)
            }

            verificationCodes.remove(email) // 인증 성공 시 코드 제거

            SuccessResponse(
                successCode = 200,
                successResult = true,
                data = "잘했어"
            ) as Any
        }.onErrorResume { ex ->
            handleFailure(ex)
        }
    }


    // key value 값에 들어있는 verificationCodes 1분 간격으로 만료된 코드 삭제
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    fun clearExpiredCodes() {
        val now = LocalDateTime.now()
        verificationCodes.entries.removeIf { (_, value) ->
            Duration.between(value.second, now).toMinutes() > 5
        }
        println("메모리에서 코드 삭제됨: ${verificationCodes.size}")
    }

    private fun handleFailure(ex: Throwable): Mono<Any> {
        val failResponse = when (ex) {
            is CustomException -> FailResponse(
                failCode = ex.statusCode,
                failResult = false,
                data = ex.message
            )
            else -> FailResponse(
                failCode = ErrorException.SERVER_ERROR.statusCode,
                failResult = false,
                data = ErrorException.SERVER_ERROR.message ?: "Unknown error occurred."
            )
        }
        return Mono.just(failResponse as Any)
    }

    private fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }


    private fun sendEmail(email: String, code: String): Mono<Void> {
        return Mono.fromCallable {
            try {
                val mimeMessage = mailSender.createMimeMessage()
                val helper = MimeMessageHelper(mimeMessage, "UTF-8")

                helper.setTo(email)
                helper.setSubject("Orbit 인증번호를 확인해주세요.")
                helper.setText("인증번호 : $code")
                helper.setFrom("Orbit <skaehgus113@naver.com>")

                mailSender.send(mimeMessage)
                println("Email successfully sent to $email")
            } catch (e: Exception) {
                println("Failed to send email to $email: ${e.message}")
                throw e
            }
        }.then()
    }
}