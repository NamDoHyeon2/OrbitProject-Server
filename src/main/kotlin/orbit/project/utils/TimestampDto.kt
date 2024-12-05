package orbit.project.utils

import java.time.LocalDateTime

open class TimestampDto(
    var createdAt: LocalDateTime ,
    var updatedAt: LocalDateTime  // 기본값을 현재 시간으로 설정
)