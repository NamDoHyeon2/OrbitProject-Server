package orbit.project.main.service

import orbit.project.main.http.AlarmResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AlarmImportService {
    fun getAlarm(): Mono<List<AlarmResponse>> {
        return Mono.just(
            listOf(
                AlarmResponse(
                    id = 12131,
                    name = "알람1",
                    project = "Orbit"
                )
            )
        )
    }
}