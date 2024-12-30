package orbit.project.config.sink.group

import orbit.project.group.http.GroupRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks


@Configuration
class SinkConfig {

    // 'replay().latest()' 설정된 Sinks.Many<GroupRequest> 생성
    @Bean
    fun groupRequestSink(): Sinks.Many<GroupRequest> {
        return Sinks.many().replay().latest<GroupRequest>()
    }

    // Sinks.Many<GroupRequest>로부터 Flux<GroupRequest>를 변환
    @Bean
    fun groupRequestFlux(groupRequestSink: Sinks.Many<GroupRequest>): Flux<GroupRequest> {
        return groupRequestSink.asFlux()
    }

}
