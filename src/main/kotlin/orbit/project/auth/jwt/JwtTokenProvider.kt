package orbit.project.auth.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import orbit.project.auth.http.LoginTokenResponse
import orbit.project.member.models.MemberEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import javax.crypto.SecretKey



@Component
class JwtTokenProvider {

    @Value("\${orbit.security.jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${orbit.security.jwt.expiration}")
    private var expirationDays: Long = 3 // 기본값 3일

    fun generateToken(loginUserInfo: MemberEntity): LoginTokenResponse {
        return LoginTokenResponse(
            memberId = loginUserInfo.memberId!!,
            loginId = loginUserInfo.loginId,
            grantType = "Bearer",
            authToken = createJWTToken(loginUserInfo),
            authTokenExpiredAt = calculateTokenExpirationTime()  // 만료 시간 계산
        )
    }

    private fun createJWTToken(member: MemberEntity): String {
        val claims = createClaims(member)
        val now = Date()
        val expirationTime = Date(now.time + expirationDays * 24 * 60 * 60 * 1000)  // 3일 후의 만료 시간을 계산

        return Jwts.builder()
            .claims(claims)  // Claims
            .id(UUID.randomUUID().toString())  // 고유 ID
            .subject(member.loginId)  // 사용자 로그인 ID
            .issuer("Orbit")  // 발급자
            .issuedAt(now)  // 발급 시간
            .expiration(expirationTime)  // 만료 시간 설정 (3일 후)
            .signWith(getSigningKey())
            .compact()
    }

    // 정보 관리 부분
    private fun createClaims(member: MemberEntity): Map<String, Any> {
        val claims = mutableMapOf<String, Any>()
        claims["loginId"] = member.loginId
        claims["memberId"] = member.memberId!!
        //TODO Group 정보 추가

        return claims
    }
    //암호화 관리 부분
    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(this.secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    // 토큰 만료 시간 계산
    private fun calculateTokenExpirationTime(): LocalDateTime {
        return LocalDateTime.now().plusDays(expirationDays)  // 기본값은 3일
    }

}
