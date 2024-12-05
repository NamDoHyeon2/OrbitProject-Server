package orbit.project.group.controller

import orbit.project.group.http.GroupRequest
import orbit.project.group.service.GroupService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/groups")
class GroupController(
    private val groupService : GroupService
) {

    @PostMapping
    fun createGroup(@RequestBody request: GroupRequest) =
        groupService.saveGroup(request)


//
//    // READ: 특정 그룹 조회
//    @GetMapping("/{groupId}")
//    fun getGroup(@PathVariable groupId: Long): ResponseEntity<GroupResponse> {
//
//    }

    // READ: 전체 그룹 목록 조회
//    @GetMapping
//    fun getAllGroups(): ResponseEntity<List<GroupResponse>> {
//        val groups = groupService.getAllGroups()
//        return ResponseEntity.ok(groups)
//    }
//
//    // UPDATE: 특정 그룹 수정
//    @PutMapping("/{groupId}")
//    fun updateGroup(
//        @PathVariable groupId: Long,
//        @RequestBody request: UpdateGroupRequest
//    ): ResponseEntity<GroupResponse> {
//        val updatedGroup = groupService.updateGroup(groupId, request)
//        return ResponseEntity.ok(updatedGroup)
//    }
//
//    // DELETE: 특정 그룹 삭제
//    @DeleteMapping("/{groupId}")
//    fun deleteGroup(@PathVariable groupId: Long): ResponseEntity<Void> {
//        groupService.deleteGroup(groupId)
//        return ResponseEntity.noContent().build()
//    }

}