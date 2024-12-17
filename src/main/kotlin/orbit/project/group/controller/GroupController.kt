package orbit.project.group.controller

import orbit.project.group.service.GroupService
import orbit.project.member.models.MemberEntity
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux


@RestController
@RequestMapping("/api/groups")
class GroupController(
    private val groupService : GroupService
) {

    @GetMapping
    fun findAllGroups(): Flux<String> {
        return Flux.just("Group1", "Group2", "Group3", "Group4")
    }

}