package orbit.project.group.service

import orbit.project.group.http.GroupRequest
import org.springframework.stereotype.Service

@Service
class GroupService {

    // CREATE
    fun saveGroup(request: GroupRequest) {
        // 그룹 생성 로직
    }

    // READ
    fun findGroupById(groupId: Long) {
        // ID로 그룹 조회 로직
    }

    fun findAllGroups() {
        // 전체 그룹 조회 로직
    }

    // DELETE
    fun deleteGroupById(groupId: Long) {
        // 그룹 삭제 로직
    }
}