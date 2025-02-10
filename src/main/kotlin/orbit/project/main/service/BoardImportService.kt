package orbit.project.main.service

import orbit.project.main.http.BoardResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BoardImportService {
    fun getBoard(): Mono<List<BoardResponse>> {
        return Mono.just(
            listOf(
                BoardResponse(
                    id = 123123,
                    name = "보드1",
                    type = "개발",
                    summation = "보드 설명"
                )
            )
        )
    }
}