package orbit.project.todo.models

import com.fasterxml.jackson.annotation.JsonFormat
import orbit.project.utils.TimestampDto
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneId

@Table("todo")
data class TodoEntity(
    @Id
    val todo_id : Long? = null,
    val maintask : String,
    val task : String,
    val detail : String,
    val tags : String,
    val is_check : Int,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val date : LocalDateTime,

    val createdBy: Long
    ) : TimestampDto(
    LocalDateTime.now(ZoneId.of("UTC")),
    LocalDateTime.now(ZoneId.of("UTC")),
) {

}