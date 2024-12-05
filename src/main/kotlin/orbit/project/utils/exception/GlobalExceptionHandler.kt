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
    fun handleGeneralException(exception: Exception): ResponseEntity<Map<String, String>> {
        val errorResponse = mapOf(
            "errorCode" to "INTERNAL_SERVER_ERROR",
            "errorMessage" to exception.localizedMessage
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse)
    }
}
