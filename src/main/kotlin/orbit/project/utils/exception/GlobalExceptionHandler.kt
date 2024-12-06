package orbit.project.utils.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(exception: CustomException): ResponseEntity<Map<String, Any>> {
        val errorResponse = mapOf(
            "errorCode" to exception.code,
            "errorMessage" to exception.message,
            "statusCode" to exception.statusCode
        )
        return ResponseEntity
            .status(exception.statusCode)
            .body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Map<String, String>> {
        val response = mapOf(
            "error" to "INTERNAL_SERVER_ERROR",
            "message" to "서버 오류가 발생했습니다. 다시 시도해주세요."
        )
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
    }
}
