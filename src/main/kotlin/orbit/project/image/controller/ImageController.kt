package orbit.project.image.controller

import jakarta.annotation.Resource
import orbit.project.image.http.ImageResponse
import orbit.project.image.service.ImageService
import orbit.project.utils.exception.CustomException
import orbit.project.utils.exception.ErrorException
import orbit.project.utils.success.SuccessResponse
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.nio.file.Files
import java.nio.file.Paths


@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageService: ImageService

) {

    // C: 이미지 생성
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestPart("file") file: Mono<FilePart>): Mono<SuccessResponse<ImageResponse>> {
        return imageService.saveImage(file)
            .map {
                imageResponse -> SuccessResponse(successCode = 200, successResult = true, data = imageResponse)
            }
    }



    @GetMapping("/{imageUniqueId}", produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE])  // 지원되는 이미지 MIME 타입
    fun getImage(@PathVariable imageUniqueId: String): Mono<ResponseEntity<*>> {
        return imageService.findImageInfoByImageUniqueId(imageUniqueId)
            .flatMap { imageInfo ->
                val imagePath = Paths.get(imageInfo.imagePath, "$imageUniqueId.jpg")  // 파일 확장자는 .jpg로 가정
                Mono.defer {
                    if (!Files.exists(imagePath)) {
                        throw CustomException(ErrorException.FILE_IMAGE_NOT_FOUND)
                    }
                    val imageResource = FileSystemResource(imagePath)
                    Mono.just(ResponseEntity.ok().body(imageResource))
                }
            }
    }



//    // R: 모든 이미지 조회
//    @GetMapping
//    fun getAllImages(): ResponseEntity<List<ImageResponse>> {
//        // 이미지 리스트 조회 로직
//        return ResponseEntity.ok(imageList)
//    }
//
//    // R: 특정 이미지 조회
//    @GetMapping("/{id}")
//    fun getImageById(@PathVariable id: Long): ResponseEntity<ImageResponse> {
//        // 특정 이미지 조회 로직
//        return ResponseEntity.ok(image)
//    }
//
//    // U: 이미지 업데이트
//    @PutMapping("/{id}")
//    fun updateImage(
//        @PathVariable id: Long,
//        @RequestBody request: ImageRequest
//    ): ResponseEntity<ImageResponse> {
//        // 이미지 업데이트 로직
//        return ResponseEntity.ok(updatedImage)
//    }
//
//    // D: 이미지 삭제
//    @DeleteMapping("/{id}")
//    fun deleteImage(@PathVariable id: Long): ResponseEntity<Void> {
//        // 이미지 삭제 로직
//        return ResponseEntity.noContent().build()
//    }

}