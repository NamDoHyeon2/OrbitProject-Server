package orbit.project.utils.exception


enum class ErrorException(val message: String, val statusCode: Int) {
    INVALID_LOGIN_ID("로그인 ID는 비워둘 수 없습니다.", 400),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", 404),
    INVALID_PASSWORD("잘못된 비밀번호입니다.", 401),
    SERVER_ERROR("서버 오류가 발생했습니다.", 500);
}
