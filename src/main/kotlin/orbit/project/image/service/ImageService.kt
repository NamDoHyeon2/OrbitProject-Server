package orbit.project.image.service

import orbit.project.auth.utils.UserActivityService
import orbit.project.image.enums.LinkedResourceType
import orbit.project.image.http.ImageResponse
import orbit.project.image.models.ImageEntity
import orbit.project.image.repository.ImageRepository
import orbit.project.utils.exception.ErrorException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.UUID

@Service
class ImageService(
    private val imageRepository : ImageRepository
) {

    @Value("\${orbit.image.base.url}")
    lateinit var imageBaseUrl: String

    @Value("\${orbit.resource.path}")
    lateinit var imageResourcePath: String

    fun saveImage(file: Mono<FilePart>): Mono<ImageResponse> {
        val createImageUniqueId = UUID.randomUUID().toString() //이미지를 식별하기위한 유니크 아이디

        return file.flatMap { filePart ->
            createImageDetails(filePart , createImageUniqueId)  // 이미지 상세 정보 생성
                .flatMap { imageDetails -> imageRepository.save(imageDetails) } // 이미지를 저장
                .map { savedImage -> ImageResponse(
                        imageId = savedImage.imageId!!,   // 저장된 이미지의 ID
                        imageUrl = savedImage.imageUrl)   // 저장된 이미지 URL
                }
        }
    }

    private fun createImageDetails(filePart: FilePart , imageUniqueId: String): Mono<ImageEntity> {
        return UserActivityService.getCurrentUserEmail()
            .flatMap { createdBy ->
                val imageName = filePart.filename()
                val imageUrl = "$imageBaseUrl/$imageUniqueId"
                val imagePath = createImageToResource(filePart,imageUniqueId)
                val linkedResourceType = LinkedResourceType.PROFILE.description

                val finalCreatedBy = createdBy ?: "ERROR"

                // Mono.defer() 사용하여 객체 생성 지연 -> UserActivityService.getCurrentUserEmail()를 가지고올때까지 대기
                Mono.defer { Mono.just(
                    ImageEntity(
                        imageId = null,
                        imageName = imageName,
                        imagePath = imagePath,
                        imageUrl = imageUrl,
                        imageUniqueId = imageUniqueId,
                        linkedResource = linkedResourceType,
                        createdBy = finalCreatedBy
                        )
                    )
                }
            }
    }

    @Throws(IOException::class)
    private fun createImageToResource(filePart: FilePart ,imageUniqueId : String): String {
        val uniqueFileName = "${imageUniqueId}.${filePart.filename().split(".").last()}"

        // 파일 저장 경로 생성
        val targetLocation: Path = Paths.get(imageResourcePath, uniqueFileName)

        // 대상 디렉토리가 없으면 생성
        val targetDir = targetLocation.parent
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir)
        }
        // 파일 저장 (FilePart에서 inputStream을 가져와 파일 복사)
        filePart.transferTo(targetLocation.toFile()).block()  // transferTo로 파일을 저장

        // 저장된 이미지의 URL 반환 (파일 경로)
        return imageResourcePath
    }

    fun findImageInfoByImageUniqueId(imageUniqueId: String): Mono<ImageEntity> =
        imageRepository.findByImageUniqueId(imageUniqueId)
            .map { it }

}