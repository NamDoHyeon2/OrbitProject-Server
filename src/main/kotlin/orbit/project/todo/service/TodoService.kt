package orbit.project.todo.service

import orbit.project.todo.http.TodoRequest
import orbit.project.todo.models.TodoEntity
import orbit.project.todo.Repository.TodoRepository
import orbit.project.member.repository.MemberRepository
import orbit.project.auth.jwt.JwtTokenValidator
import orbit.project.auth.utils.UserActivityService
import orbit.project.utils.success.SuccessResponse
import orbit.project.utils.fail.FailResponse
import orbit.project.utils.exception.ErrorException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Service
class TodoService(
    private val todoRepository: TodoRepository,
    private val memberRepository: MemberRepository
) {
    fun saveTodo(todoRequest: TodoRequest): Mono<Any> {
        return UserActivityService.getMemberIdFromSecurityContext() //
            .flatMap { memberId ->
                memberRepository.findByMemberId(memberId)
                    .flatMap { member ->
                        val todoEntities = todoRequest.subTaskList.map { subTask ->
                            TodoEntity(
                                maintask = todoRequest.mainTask,
                                task = subTask.task,
                                detail = subTask.details ?: "",
                                tags = subTask.tags?.joinToString(",") ?: "",
                                is_check = subTask.onChecked,  // ✅ check 값이 null인지 확인
                                date = LocalDateTime.parse(todoRequest.date + "T00:00:00"),
                                createdBy = memberId.toLong()
                            )
                        }

                        todoRepository.saveAll(todoEntities)
                            .collectList()
                            .map { savedTasks ->
                                SuccessResponse(
                                    successCode = 200,
                                    successResult = true,
                                    data = savedTasks
                                ) as Any
                            }
                    }
            }
            .onErrorResume { error ->
                val errorResponse = when (error) {
                    is IllegalArgumentException -> ErrorException.INVALID_REQUEST_TIME
                    is NoSuchElementException -> ErrorException.NOT_DATA
                    else -> ErrorException.SERVER_ERROR
                }

                Mono.just(
                    FailResponse(
                        failCode = errorResponse.statusCode,
                        failResult = false,
                        data = errorResponse.message
                    ) as Any
                )
            }
    }



}
