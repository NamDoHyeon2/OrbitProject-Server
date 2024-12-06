package orbit.project.auth.jwt

import CustomUserDetails
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import orbit.project.auth.principal.CustomReactiveUserDetailsService
import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import javax.crypto.SecretKey


@Service
class JwtTokenValidator(
    private val customReactiveUserDetailsService: CustomReactiveUserDetailsService
){

    @Value("\${orbit.security.jwt.secret}")
    private lateinit var secretKey: String

    private val logger = LoggerFactory.getLogger(JwtTokenValidator::class.java)

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(this.secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    @Throws(CustomException::class)
    fun validateJwtToken(jwtToken: String?): Boolean {
        try {
            println(jwtToken)
            val claimsJws: Jws<Claims> = Jwts.parser().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(jwtToken)

            return true
        } catch (e: ExpiredJwtException) {
            // JWT 토큰이 만료
            logger.error("JWT 토큰이 만료되었습니다: {}", e.message)
            throw CustomException(ErrorException.TOKEN_EXPIRED)
        } catch (e: UnsupportedJwtException) {
            // JWT 토큰이 지원 x
            logger.error("지원되지 않는 JWT 토큰입니다: {}", e.message)
            throw CustomException(ErrorException.FORBIDDEN)
        } catch (e: JwtException) {
            // JWT 서명 오류 및 기타 JWT 예외 처리
            logger.error("유효하지 않은 JWT 토큰입니다: {}", e.message)
            throw CustomException(ErrorException.UNAUTHORIZED)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT 클레임 문자열이 비어 있습니다: {}", e.message)
            throw CustomException(ErrorException.INVALID_PASSWORD)
        }
    }


    fun getAuthentication(token: String): Mono<Authentication> {
        // 토큰을 검증하여 Claims를 추출
        val claimsJws: Jws<Claims> = Jwts.parser().setSigningKey(getSigningKey()).build()
            .parseClaimsJws(token)

        return getAuthenticationByClaims(claimsJws.payload)
    }

    fun getAuthenticationByClaims(claims: Claims): Mono<Authentication> {
        // Claims에서 loginId와 memberId를 추출하고, 없으면 예외 발생
        val loginId = claims["loginId"] ?: throw CustomException(ErrorException.LOGIN_ID_NOT_FOUND)
        val memberId = claims["memberId"] ?: throw CustomException(ErrorException.MEMBER_ID_NOT_FOUND)

        return customReactiveUserDetailsService.findByUsername(loginId as String)
            .map { userDetails ->
                UsernamePasswordAuthenticationToken(userDetails, userDetails.password, userDetails.authorities)
            }
    }

}