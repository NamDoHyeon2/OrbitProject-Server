package orbit.project.utils.exception


class CustomException(
    val errorException: ErrorException,
    val code: String = errorException.name,
    override val message: String = errorException.message
) : RuntimeException(message) {

    val statusCode: Int = errorException.statusCode

    override fun toString(): String {
        return "CustomException(code='$code', message='$message', statusCode=$statusCode)"
    }
}
