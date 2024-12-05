package orbit.project.config.securityException


import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class CustomServerAccessDeniedHandler : ServerAccessDeniedHandler {

    private val objectMapper = ObjectMapper()  // ObjectMapper 인스턴스를 생성

    override fun handle(exchange: ServerWebExchange, denied: AccessDeniedException?): Mono<Void> {
        // 403 Forbidden 상태 코드 설정
        exchange.response.statusCode = HttpStatus.FORBIDDEN

        // JSON 형식으로 오류 객체 생성
        val errorResponse = mapOf(
            "error" to "Forbidden",
            "message" to "You do not have permission to access this resource"
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