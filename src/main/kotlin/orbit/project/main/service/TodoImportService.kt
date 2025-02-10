package orbit.project.main.service

import orbit.project.main.http.SubTaskResponse
import orbit.project.main.http.TodoListResponse
import orbit.project.todo.Repository.TodoRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TodoImportService (
    private val todoRepository: TodoRepository
) {
    fun getTodoList(memberId: Long): Mono<List<TodoListResponse>> {
        return todoRepository.findByCreatedBy(memberId)
            .collectList()
            .map { todos ->
                todos.groupBy { it.maintask }.map { (mainTask, taskList) ->
                    TodoListResponse(
                        mainTask = mainTask,
                        subTaskList = taskList.map { todo ->
                            SubTaskResponse(
                                task = todo.task,
                                details = todo.detail,
                                tags = todo.tags.split(","),
                                onChecked = todo.is_check
                            )
                        },
                        date = taskList.firstOrNull()?.date.toString()
                    )
                }
            }
    }
}
