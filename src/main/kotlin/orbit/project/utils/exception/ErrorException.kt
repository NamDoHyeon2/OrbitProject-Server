package orbit.project.utils.exception


enum class ErrorException(val message: String, val statusCode: Int) {
    INVALID_LOGIN_ID("로그인 ID는 비워둘 수 없습니다.", 400),
    MEMBER_NOT_FOUND("사용자를 찾을 수 없습니다.", 404),
    INVALID_PASSWORD("잘못된 비밀번호입니다.", 401),
    SERVER_ERROR("서버 오류가 발생했습니다.", 500),
    UNAUTHORIZED("인증 권한이 없습니다.", 403), // 인증 권한 없음
    TOKEN_EXPIRED("토큰이 만료되었습니다.", 401), // 토큰 만료
    FORBIDDEN("접근이 금지되었습니다.", 403),// 접근이 금지됨
    LOGIN_ID_NOT_FOUND("로그인 아이디가 토큰에 존재하지 않습니다.", 400), // 로그인 아이디가 없는 경우
    MEMBER_ID_NOT_FOUND("멤버 아이디가 토큰에 존재하지 않습니다.", 400) // 멤버 아이디가 없는 경우
}
