package orbit.project.config.email

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class EmailConfig {

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()

        // SMTP 서버 설정
        mailSender.host = "smtp.naver.com"
        mailSender.port = 587
        mailSender.username = "skaehgus113@naver.com" // 네이버 이메일 주소
        mailSender.password = "DG2HRF1F67DB" // 앱 비밀번호
        mailSender.defaultEncoding = "UTF-8"

        // JavaMail 속성 설정
        val properties = Properties()
        properties["mail.smtp.auth"] = true
        properties["mail.smtp.starttls.enable"] = true
        properties["mail.smtp.ssl.enable"] = false
        properties["mail.smtp.connectiontimeout"] = 5000
        properties["mail.smtp.timeout"] = 5000
        properties["mail.smtp.writetimeout"] = 5000
        properties["mail.debug"] = "true" // 디버깅 활성화

        mailSender.javaMailProperties = properties

        return mailSender
    }
}
