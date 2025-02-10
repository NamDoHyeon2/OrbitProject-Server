package orbit.project.todo.controller

import orbit.project.todo.http.TodoRequest
import orbit.project.todo.service.TodoService
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class TodoController(
    private val todoService: TodoService,
) {

    @PostMapping("/TodoList")
    fun saveTodoList(@RequestBody todoRequest: TodoRequest): Mono<Any> {
        return todoService.saveTodo(todoRequest)
    }
}