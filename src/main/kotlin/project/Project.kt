package project

import time.TimeEntry
import time.secondsToFormattedString
import java.time.LocalDate
import java.time.LocalDateTime

data class Day(
    val date: LocalDateTime,
    val project: List<Project>,
) {
    val totalDurationFormatted by lazy {
        project.sumOf { it.totalDurationSeconds }
            .secondsToFormattedString()
    }
}

data class Project(
    val name: String,
    val timeEntries: Set<TimeEntry>,
) {
    val totalDurationSeconds by lazy {
        timeEntries
            .filter { it.day == LocalDate.now().atStartOfDay() }
            .sumOf { it.duration.seconds }
    }

    val totalDurationTodayFormatted by lazy {
        totalDurationSeconds
            .secondsToFormattedString()
    }
}
