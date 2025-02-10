package orbit.project.config.kakao

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class KakaoConfig {
    @Value("\${kakao.auth.url}")
    lateinit var authUrl: String

    @Value("\${kakao.api.url}")
    lateinit var apiUrl: String

    @Value("\${kakao.client.id}")
    lateinit var clientId: String

    @Value("\${kakao.client.secret}")
    lateinit var clientSecret: String

    @Value("\${kakao.redirect.url}")
    lateinit var redirectUrl: String

    val redirectionUrl: String
        get() = "${authUrl}/oauth/authorize" +
                "?client_id=${clientId}" +
                "&redirect_uri=${redirectUrl}" +
                "&response_type=code"
}
