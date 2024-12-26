package orbit.project.utils.success

data class SuccessResponse<T>(
    val code: Int,      // 성공 코드를 나타냄 (예: "200")
    val message: Boolean,   // 성공 메시지를 나타냄 (예: "Operation successful")
    val data: T? = null          // 제네릭 타입 데이터를 포함 (객체나 객체 리스트)
)
