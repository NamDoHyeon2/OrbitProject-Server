package orbit.project.project.models

import orbit.project.project.http.ProjectRequest
import orbit.project.utils.TimestampDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneId

//프로젝트 단일일 경우 모델
@Table(name = "project")
data class ProjectEntity(

    @Id
    @Column("project_id")
    val projectId : Long? = null,

    @Column("project_name")
    val projectName: String,

    @Column("project_description")
    val projectDescription: String,

    @Column("created_by") // created_by 컬럼에 매핑
    var createdBy: String? = null,

) : TimestampDto(
    LocalDateTime.now(ZoneId.of("UTC")),
    LocalDateTime.now(ZoneId.of("UTC")),
) {
    companion object{
        fun projectRequestConvertProjectEntity(projectRequest: ProjectRequest): ProjectEntity =
            ProjectEntity(
                projectId = null,
                projectName = projectRequest.projectName,
                projectDescription = projectRequest.projectDescription,
                createdBy = null
            )
    }
}