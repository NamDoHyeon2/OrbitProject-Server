package orbit.project.main.http

data class PlannerResponse(
    val name: String,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val color: String
)