package orbit.project.config.securityException

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class CustomServerAuthenticationEntryPoint : ServerAuthenticationEntryPoint {
    private val objectMapper = ObjectMapper()  // ObjectMapper 인스턴스를 생성

    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException?): Mono<Void> {
        // 401 Unauthorized 상태 코드 설정
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED

        // JSON 형식으로 오류 객체 생성
        val errorResponse = mapOf(
            "error" to "Unauthorized",
            "message" to "Authentication is required to access this resource"
        )

        // 오류 객체를 JSON 형식으로 직렬화
        val jsonResponse = objectMapper.writeValueAsBytes(errorResponse)

        // 응답 본문에 JSON 형식의 오류 메시지를 추가
        return exchange.response.writeWith(
            Mono.just(
                exchange.response.bufferFactory().wrap(jsonResponse)
            )
        )
    }

}