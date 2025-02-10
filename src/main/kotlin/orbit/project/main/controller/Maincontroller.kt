package orbit.project.main.controller

import orbit.project.main.service.MainService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth")
class MainDataController(
    private val mainService: MainService
) {

    @GetMapping("/MainData")
    fun getMainData(@RequestParam resources: List<String>): Mono<Any> {
        return mainService.getMainData(resources)
    }
}