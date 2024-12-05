package orbit.project.group.models

import orbit.project.utils.TimestampDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneId


@Table("Group") // 실제 DB 테이블 이름
class GroupEntity(
    @Id
    @Column("group_id")
    val groupId: Int? = null, // auto_increment를 위해 nullable 처리

    @Column("group_name")
    val groupName: String,
): TimestampDto(
    LocalDateTime.now(ZoneId.of("UTC")),
    LocalDateTime.now(ZoneId.of("UTC")),
){





}
