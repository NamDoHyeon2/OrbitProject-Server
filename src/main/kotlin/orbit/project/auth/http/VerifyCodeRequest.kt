package orbit.project.auth.http

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class VerifyCodeRequest(

    val email: String,

    val code: String,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val requestTime: LocalDateTime
)