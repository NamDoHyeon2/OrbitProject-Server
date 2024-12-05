package orbit.project.auth.http

data class LoginRequest(
    val loginId: String,
    val password : String
) {
}