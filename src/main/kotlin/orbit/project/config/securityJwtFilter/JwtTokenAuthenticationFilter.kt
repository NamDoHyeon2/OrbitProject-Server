package orbit.project.config.securityJwtFilter

import orbit.project.auth.jwt.JwtTokenValidator
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

private const val HEADER_PREFIX = "Bearer"

class JwtTokenAuthenticationFilter(
    private val jwtValidator : JwtTokenValidator,
    private val securityContextRepository: ServerSecurityContextRepository
) : WebFilter  {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = resolveToken(exchange.request)

        return if (!token.isNullOrEmpty() && jwtValidator.validateJwtToken(token)) {
            val authenticationMono = jwtValidator.getAuthentication(token)

            authenticationMono.flatMap { authentication ->
                val securityContext = SecurityContextImpl(authentication)

                securityContextRepository.save(exchange, securityContext)
                    .then(chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext))))
            }
        } else {
            chain.filter(exchange)
        }
    }

    //토큰 얻는 함수
    //Authorization: Bearer <token> 다음과 같은 형식으로 들어옴
    private fun resolveToken(request : ServerHttpRequest): String? {
        //헤더에서 Bearer 토큰을 추출
        val bearerToken = request.headers.getFirst("Authorization") ?: return null
        //해당 값에서 Bearer 뒤에 부분 뽑기
        return if (bearerToken.startsWith("$HEADER_PREFIX ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }

}