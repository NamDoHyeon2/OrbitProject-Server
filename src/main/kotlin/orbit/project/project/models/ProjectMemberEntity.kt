package orbit.project.project.models

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneId

@Table("project_member")
data class ProjectMemberEntity(
    @Column("project_id")
    val projectId: Long,
    @Column("member_id")
    val memberId : Long,
    @Column("joined_at")
    val joinedAt : LocalDateTime,
){
    companion object{
        fun convertProjectIdMemberIdToProjectMemberEntity(projectId: Long , memberId: Long) =
            ProjectMemberEntity(
                projectId = projectId,
                memberId = memberId,
                joinedAt = LocalDateTime.now(ZoneId.of("UTC"))
            )
    }
}
