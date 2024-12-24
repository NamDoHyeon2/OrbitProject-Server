package orbit.project.email.http

data class VerifyCodeRequest(
    val email: String,
    val code: String
)