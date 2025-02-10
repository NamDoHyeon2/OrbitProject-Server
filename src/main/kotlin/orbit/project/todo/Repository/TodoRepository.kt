package orbit.project.todo.Repository

import orbit.project.todo.models.TodoEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface TodoRepository : ReactiveCrudRepository<TodoEntity, Long> {

    fun findByCreatedBy(createdBy: Long): Flux<TodoEntity>
}