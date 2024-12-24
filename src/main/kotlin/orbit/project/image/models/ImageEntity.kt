package orbit.project.image.models

import orbit.project.utils.TimestampDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.ZoneId

@Table("image")
data class ImageEntity(

    @Id
    @Column("image_id")  // 컬럼명 명시적으로 지정
    val imageId: Long? = null,

    @Column("image_name") // image_name 컬럼에 매핑
    val imageName: String,

    @Column("image_path") // image_path 컬럼에 매핑
    val imagePath: String,

    @Column("image_unique_id")
    val imageUniqueId: String,

    @Column("image_url") // image_url 컬럼에 매핑
    val imageUrl: String,

    @Column("linked_resource") // linked_resource 컬럼에 매핑
    val linkedResource: String,

    @Column("created_by") // created_by 컬럼에 매핑
    val createdBy: String

) : TimestampDto(
    LocalDateTime.now(ZoneId.of("UTC")),
    LocalDateTime.now(ZoneId.of("UTC")),
)
