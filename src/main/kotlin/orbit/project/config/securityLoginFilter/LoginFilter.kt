package orbit.project.config.securityLoginFilter

import orbit.project.auth.jwt.JwtTokenProvider
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class LoginFilter (tokenProvider: JwtTokenProvider) : WebFilter{

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        // 필터 체인을 이어서 실행
        return chain.filter(exchange)
    }


}