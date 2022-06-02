package time

import java.time.Duration
import java.time.LocalDateTime

data class TimeEntry(
    val id: Int? = null,
    val projectName: String,
    val description: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val day: LocalDateTime? = null,
) {
    val duration: Duration by lazy { Duration.between(start, end) }
}
