package orbit.project.group.service

import orbit.project.group.http.GroupRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GroupService {

    val groupList = mutableListOf<GroupRequest>()

    fun createInitialGroups() : List<GroupRequest> {
        return groupList
    }

    fun initializeData() {
        if (groupList.isEmpty()) {
            (1..5).map {
                groupList.add(GroupRequest(UUID.randomUUID(), "Initial Group $it", true, true))
            }
        }
    }


    fun saveGroup(group: GroupRequest){
        group.groupId = UUID.randomUUID()
        groupList.add(group)
    }
}