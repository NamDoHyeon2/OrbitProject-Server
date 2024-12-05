package orbit.project.config

import orbit.project.config.securityException.CustomServerAccessDeniedHandler
import orbit.project.config.securityException.CustomServerAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter


@Configuration
@EnableWebFluxSecurity // WebFlux 보안 활성화
class SpringSecurityConfig {

    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        // 폼 로그인과 기본 HTTP 인증을 비활성화
        http.csrf { auth -> auth.disable() }
        http.formLogin { auth -> auth.disable() }
        http.httpBasic { auth -> auth.disable() }

        // 세션 STATELESS 설정
        http.securityContextRepository(WebSessionServerSecurityContextRepository())

        //인증 실패시 처리
        http.exceptionHandling {
            it.authenticationEntryPoint(CustomServerAuthenticationEntryPoint()) // 인증 실패 시 처리
            it.accessDeniedHandler(CustomServerAccessDeniedHandler()) // 권한 부족 시 처리
        }

        //인증 경로
        http.authorizeExchange{ auth ->
            auth
                .pathMatchers("/api/auth/login","/api/members/register").permitAll() // 인증이 필요 없는 경로 설정
                .anyExchange().authenticated() // 나머지 경로는 인증 필수
        }

        //익명 사용자 비 활성화
        //로그아웃 비 활성화
        http.anonymous { auth -> auth.disable() }
        http.logout { auth -> auth.disable() }

        //Iframe 막기
        http.headers { headers ->
            headers.frameOptions { frameOptions ->
                frameOptions.mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN)
            }
        }
        //로그인 진행을 위한 로그인 필터 추가 예정

//        http.addFilterBefore(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)

        return http.build()
    }


}
