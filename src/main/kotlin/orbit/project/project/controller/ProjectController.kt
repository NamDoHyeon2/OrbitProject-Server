package orbit.project.project.controller

import orbit.project.project.http.ProjectRequest
import orbit.project.project.http.ProjectResponse
import orbit.project.project.service.ProjectService
import orbit.project.utils.success.SuccessResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/projects")
class ProjectController(
    private val projectService: ProjectService
) {

    @PostMapping
    fun addProject(@RequestBody projectRequest: ProjectRequest) : Mono<SuccessResponse<ProjectResponse>> =
        projectService.saveProject(projectRequest)
            .map { projectResponse -> SuccessResponse(code = 200, message = true, data = projectResponse) }


}