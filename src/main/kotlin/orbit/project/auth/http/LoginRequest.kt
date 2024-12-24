package orbit.project.auth.http

data class LoginRequest(
    val email: String,
    val password : String
) {
}