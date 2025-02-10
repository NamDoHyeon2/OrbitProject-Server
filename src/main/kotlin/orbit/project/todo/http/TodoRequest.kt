package orbit.project.todo.http

data class TodoRequest(
    val mainTask: String,
    val subTaskList: List<SubTask>,  // 하위 할 일 목록
    val date: String  // YYYY-MM-DD 형식
)

data class SubTask(
    val task: String,
    val details: String? = null,  // 선택적 필드 (없을 수 있음)
    val tags: List<String>? = null,  // 선택적 필드 (없을 수 있음)
    val onChecked: Int
)
