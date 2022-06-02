package time

import java.time.Duration
import java.time.LocalDateTime

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
        val delta = System.currentTimeMillis() - startMillis!!
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
