package orbit.project.utils.fail

data class FailResponse<T>(
    val failCode: Int,      // 실패 코드를 나타냄 (예: "200")
    val failResult: Boolean,   // 실패 메시지를 나타냄 (예: "Operation successful")
    val data: T? = null          // 제네릭 타입 데이터를 포함 (객체나 객체 리스트)
)