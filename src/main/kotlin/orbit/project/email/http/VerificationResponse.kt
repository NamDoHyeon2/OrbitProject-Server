package orbit.project.email.http

data class VerificationResponse(
    val success: Boolean, // 요청 성공 여부
    val message: String,   // 상세 메시지
    //val data: String // 코드
)