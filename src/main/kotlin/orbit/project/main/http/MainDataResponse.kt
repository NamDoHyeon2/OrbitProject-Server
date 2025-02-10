package orbit.project.main.http

data class MainDataResponse(
    val todoList: List<TodoListResponse>? = null,
    val planner: PlannerResponse? = null,
    val alarm: List<AlarmResponse>? = null,
    val board: List<BoardResponse>? = null
)