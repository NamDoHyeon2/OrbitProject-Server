package orbit.project.main.service

import orbit.project.main.http.PlannerResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PlannerImportService {
    fun getPlanner(): Mono<PlannerResponse> {
        return Mono.just(
            PlannerResponse(
                name = "일정 이름",
                startDate = "2025-01-02",
                endDate = "2025-01-03",
                startTime = "09:00",
                endTime = "16:00",
                color = "#fce4ec"
            )
        )
    }
}