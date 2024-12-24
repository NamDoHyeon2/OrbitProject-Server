package orbit.project.config

import orbit.project.auth.jwt.JwtTokenValidator
import orbit.project.config.securityException.CustomServerAccessDeniedHandler
import orbit.project.config.securityException.CustomServerAuthenticationEntryPoint
import orbit.project.config.securityJwtFilter.JwtTokenAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SpringSecurityConfig(
    private val jwtTokenValidator: JwtTokenValidator,
) {

    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // CORS 설정 추가
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            addAllowedOrigin("http://34.64.173.72:3000") // 허용할 Origin
            addAllowedMethod("*") // 모든 HTTP 메서드 허용
            addAllowedHeader("*") // 모든 헤더 허용
            allowCredentials = false // 쿠키를 사용하지 않을 경우 false
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration) // 모든 경로에 CORS 설정 적용
        return source
    }

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        // CORS 설정 추가
        http.cors { cors -> cors.configurationSource(corsConfigurationSource()) }

        // 기존 코드 유지
        http.csrf { auth -> auth.disable() }
        http.formLogin { auth -> auth.disable() }
        http.httpBasic { auth -> auth.disable() }

        http.securityContextRepository(WebSessionServerSecurityContextRepository())

        http.exceptionHandling {
            it.authenticationEntryPoint(CustomServerAuthenticationEntryPoint())
            it.accessDeniedHandler(CustomServerAccessDeniedHandler())
        }

        http.authorizeExchange { auth ->
            auth
                .pathMatchers(
                    "/api/auth/login",
                    "/api/auth/signup",
                    "/api/members/register",
                    "/api/sse/groups",
                    "/api/auth/SendCode",
                    "/api/auth/VerifyCode"
                ).permitAll()
                .anyExchange().authenticated()
        }

        http.anonymous { auth -> auth.disable() }
        http.logout { auth -> auth.disable() }

        http.headers { headers ->
            headers.frameOptions { frameOptions ->
                frameOptions.mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)
            }
        }

        http.addFilterBefore(
            JwtTokenAuthenticationFilter(jwtTokenValidator),
            SecurityWebFiltersOrder.AUTHENTICATION
        )

        return http.build()
    }
}
