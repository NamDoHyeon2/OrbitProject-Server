package orbit.project.project.repository

import orbit.project.project.models.ProjectEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ProjectRepository : ReactiveCrudRepository<ProjectEntity , Long>{

}