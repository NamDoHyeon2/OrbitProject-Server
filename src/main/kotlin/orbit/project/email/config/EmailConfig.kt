package orbit.project.email.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*

@Configuration
class EmailConfig {

    @Value("\${orbit.spring.mail.host}")
    private lateinit var host: String

    @Value("\${orbit.spring.mail.port}")
    private var port: Int = 0

    @Value("\${orbit.spring.mail.username}")
    private lateinit var username: String

    @Value("\${orbit.spring.mail.password}")
    private lateinit var password: String

    @Value("\${orbit.spring.mail.properties.mail.smtp.auth}")
    private var auth: Boolean = false

    @Value("\${orbit.spring.mail.properties.mail.smtp.starttls.enable}")
    private var starttlsEnable: Boolean = false

    @Value("\${orbit.spring.mail.properties.mail.smtp.ssl.enable}")
    private var sslEnable: Boolean = false

    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password
        mailSender.defaultEncoding = "UTF-8"

        val properties = Properties()
        properties["mail.smtp.auth"] = auth
        properties["mail.smtp.starttls.enable"] = starttlsEnable
        properties["mail.smtp.ssl.enable"] = sslEnable
        properties["mail.smtp.connectiontimeout"] = 5000
        properties["mail.smtp.timeout"] = 5000
        properties["mail.smtp.writetimeout"] = 5000
        mailSender.javaMailProperties = properties

        return mailSender
    }
}