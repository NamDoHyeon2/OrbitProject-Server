package orbit.project.group.controller

import orbit.project.group.http.GroupRequest
import orbit.project.group.service.GroupService
import orbit.project.utils.success.SuccessResponse
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks

@RestController
@RequestMapping("/api/sse/groups")
@CrossOrigin(origins = ["http://localhost:63342"])
class GroupController(
    private val groupService: GroupService
) {
    private val sink: Sinks.Many<GroupRequest> = Sinks.many().replay().latest()
    private val groupFlux: Flux<GroupRequest> = sink.asFlux()

    @GetMapping("/init")
    fun initData() { groupService.initializeData() }

    @GetMapping
    fun findAllGroups(): Flux<ServerSentEvent<GroupRequest>> {
        val exampleGroupList = groupService.createInitialGroups();//기존에 가지고있던 사용자의 그룹

        return Flux.merge(groupFlux, Flux.fromIterable(exampleGroupList)) //실시간으로 들어오는 그룹
            .distinctUntilChanged()  // 중복된 데이터는 필터링 (변경되지 않은 데이터는 발행하지 않음)
            .map { group ->
                ServerSentEvent.builder(group)
                    .build()
            }
            .doOnCancel {

            }
    }

    /***
        SuccessResponse<String>
        SuccessResponse<Int>
        SuccessResponse<List<String>>
        out Any 로 쓰면 다음과같이 처리됨
    ***/
    @PostMapping
    fun addGroup(@RequestBody groupRequest: GroupRequest): Mono<SuccessResponse<out Any>> {
        groupService.saveGroup(groupRequest)
        val emitResult = sink.tryEmitNext(groupRequest)

        val response = when {
            emitResult.isSuccess -> {
                SuccessResponse(200, true, groupRequest)
            }
            emitResult.isFailure -> {
                SuccessResponse(400, false, null)
            }
            else -> {
                SuccessResponse(500, false, "SERVER ERROR")
            }
        }

        return Mono.just(response)
    }




}
