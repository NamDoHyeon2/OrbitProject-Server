package orbit.project.auth.http

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class LoginTokenResponse(
    //로그인 ID
    val memberId : Long,

    val loginId: String,

    val grantType: String = "Bearer",

    val authToken: String, //긴 수명을 가지고 로그인 체크 ( 로그인 , 긴 수명 가지고 7일정도 )

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val authTokenExpiredAt : LocalDateTime,
)