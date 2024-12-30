package orbit.project.image.repository

import orbit.project.image.models.ImageEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface ImageRepository : ReactiveCrudRepository<ImageEntity , Long> {

    fun findByImageUniqueId(imageUniqueId: String): Mono<ImageEntity>

}