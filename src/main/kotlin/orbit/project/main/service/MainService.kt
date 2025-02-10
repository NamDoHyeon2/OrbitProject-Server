package orbit.project.main.service

import orbit.project.utils.success.SuccessResponse
import orbit.project.utils.fail.FailResponse
import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import orbit.project.main.http.*
import orbit.project.auth.utils.UserActivityService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MainService(
    private val todoImportService: TodoImportService,
    private val plannerImportService: PlannerImportService,
    private val alarmImportService: AlarmImportService,
    private val boardImportService: BoardImportService
) {

    fun getMainData(resources: List<String>): Mono<Any> {
        return UserActivityService.getMemberIdFromSecurityContext()
            .flatMap { memberId ->

                Mono.zip(
                    if (resources.contains("todoList")) todoImportService.getTodoList(memberId)
                        .defaultIfEmpty(emptyList()) else Mono.just(emptyList()),
                    if (resources.contains("planner")) plannerImportService.getPlanner() else Mono.empty(),
                    if (resources.contains("alarm")) alarmImportService.getAlarm() else Mono.empty(),
                    if (resources.contains("board")) boardImportService.getBoard() else Mono.empty()
                )
                    .map { tuple ->
                        val todoList = tuple.t1 as? List<TodoListResponse>
                        val planner = tuple.t2 as? PlannerResponse
                        val alarm = tuple.t3 as? List<AlarmResponse>
                        val board = tuple.t4 as? List<BoardResponse>

                        MainDataResponse(
                            todoList = todoList,
                            planner = planner,
                            alarm = alarm,
                            board = board
                        )
                    }
                    .flatMap { mainData ->
                        Mono.just<Any>(
                            SuccessResponse(
                                successCode = 200,
                                successResult = true,
                                data = mainData
                            )
                        )
                    }
            }
            .onErrorResume { e ->
                val failResponse = when (e) {
                    is CustomException -> FailResponse(failCode = e.statusCode, failResult = false, data = e.message)
                    else -> FailResponse(failCode = 500, failResult = false, data = "Unexpected error occurred")
                }
                Mono.just(failResponse as Any)
            }
    }
}
