package orbit.project.main.http

data class TodoListResponse(
    val mainTask: String,
    val subTaskList: List<SubTaskResponse>,
    val date: String
)

data class SubTaskResponse(
    val task: String,
    val details: String,
    val tags: List<String>,
    val onChecked: Int
)