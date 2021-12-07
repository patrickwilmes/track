package time

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Timer {
    var startTime: LocalDateTime? = null
        private set
    var endTime: LocalDateTime? = null
        private set
    var isRunning = false

    private var startMillis: Long? = null

    fun start() {
        isRunning = true
        startTime = LocalDateTime.now()
        startMillis = System.currentTimeMillis()
    }

    fun tick(): Duration {
        val delta = System.currentTimeMillis() - startMillis!!;
        return Duration.ofMillis(delta)
    }

    fun stop() {
        isRunning = false
        endTime = LocalDateTime.now()
    }

    fun reset() {
        startTime = null
        endTime = null
    }
}

fun LocalDateTime?.asFormattedString(): String = if (this == null) "" else format(DateTimeFormatter.ofPattern("hh:mm:ss"))
fun LocalDateTime?.asFormattedDateString(): String = if (this == null) "" else format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

fun Long.secondsToFormattedString(): String {
    val clockHours = this / 3600
    val clockMinutes = (this % 3600) / 60
    val clockSeconds = this % 60
    return String.format("%02d:%02d:%02d", clockHours, clockMinutes, clockSeconds)
}
