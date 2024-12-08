package orbit.project.group.controller

import orbit.project.group.http.GroupRequest
import orbit.project.group.service.GroupService
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.awt.PageAttributes.MediaType
import java.util.*

@RestController
@RequestMapping("/api/sse/groups")
@CrossOrigin(origins = ["http://localhost:63342"])
class GroupController(
    private val groupService: GroupService
) {
    private val sink: Sinks.Many<GroupRequest> = Sinks.many().replay().latest()
    private val groupFlux: Flux<GroupRequest> = sink.asFlux()

    @GetMapping
    fun findAllGroups(): Flux<ServerSentEvent<GroupRequest>> {
        val exampleGroupList: MutableList<GroupRequest> = mutableListOf()
        for (i in 1..10) {
            exampleGroupList.add(
                GroupRequest(UUID.randomUUID(), "TEST$i", true, true)
            )
        }

        // Flux.merge()로 두 스트림을 병합하여 실시간 데이터 스트림과 예시 데이터를 동시에 전달
        return Flux.merge(groupFlux, Flux.fromIterable(exampleGroupList))
            .map { group ->
                // ServerSentEvent로 변환
                ServerSentEvent.builder(group)
                    .build()
            }
            .doOnCancel {
                // Flux 취소 시 부수 효과 실행
                println("Flux가 취소되었습니다.")
            }
    }

    @PostMapping
    fun addGroup(@RequestBody groupRequest: GroupRequest): Mono<Void> {
        val emitResult = sink.tryEmitNext(groupRequest)
        if (emitResult.isFailure) {
            println("데이터 발행 실패: ${emitResult.isFailure()}")
        }
        return Mono.empty()
    }
}
