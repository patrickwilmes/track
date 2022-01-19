package time

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object TimeEntryTable : Table(name = "time_entry") {
    val id = integer("id").autoIncrement()
    val projectName = text("project_name")
    val description = text("description")
    val start = datetime("start")
    val end = datetime("end")
    val day = datetime("day")

    override val primaryKey = PrimaryKey(id)
}

fun getProjectNamesStartingWith(leading: String): List<String> {
    if (leading.isBlank())
        return emptyList()
    return transaction {
        TimeEntryTable.select { TimeEntryTable.projectName.like("$leading%") }
            .map {
                it[TimeEntryTable.projectName]
            }
    }
}

fun updateTimeEntry(timeEntry: TimeEntry, newDuration: Duration) {
    val newEndTime = timeEntry.start.plusSeconds(newDuration.seconds)
    val newTimeEntry = timeEntry.copy(end = newEndTime)
    transaction {
        TimeEntryTable.update({ TimeEntryTable.id eq newTimeEntry.id!! }) {
            it[projectName] = newTimeEntry.projectName
            it[description] = newTimeEntry.description
            it[end] = newTimeEntry.end
        }
    }
}

fun saveTimeEntry(timeEntry: TimeEntry, doRound: Boolean = false) {
    val entryToSave = if (doRound) {
        val duration = Duration.between(timeEntry.start, timeEntry.end).abs()
        val trunc = duration.truncatedTo(ChronoUnit.HOURS)
        val diff = duration - trunc
        val final = if (diff.seconds < 15 * 60) {
            trunc.plusMinutes(15)
        } else if (diff.seconds > 15 * 60 && diff.seconds < 30 * 60) {
            trunc.plusMinutes(30)
        } else if (diff.seconds > 30 * 60 && diff.seconds < 45 * 60) {
            trunc.plusMinutes(45)
        } else if (diff.seconds > 45 * 60) {
            trunc.plusHours(1)
        } else {
            duration
        }
        timeEntry.copy(end = timeEntry.start.plusSeconds(final.seconds))
    } else timeEntry
    transaction {
        TimeEntryTable.insert {
            it[projectName] = entryToSave.projectName
            it[description] = entryToSave.description
            it[start] = entryToSave.start
            it[end] = entryToSave.end
            it[day] = LocalDate.now().atStartOfDay()
        }
    }
}

fun deleteTimeEntryById(id: Int) {
    transaction {
        TimeEntryTable.deleteWhere { TimeEntryTable.id eq id }
    }
}

internal fun getTimeEntriesForDay(date: LocalDate): List<TimeEntry> {
    val start = date.with(DayOfWeek.MONDAY)
    val end = date.with(DayOfWeek.FRIDAY)
    return transaction {
        TimeEntryTable
            .select {
                TimeEntryTable.start.greaterEq(start.atStartOfDay()) and TimeEntryTable.end.lessEq(
                    end.atStartOfDay().plusDays(1)
                )
            }
            .map {
                TimeEntry(
                    id = it[TimeEntryTable.id],
                    projectName = it[TimeEntryTable.projectName],
                    description = it[TimeEntryTable.description],
                    start = it[TimeEntryTable.start],
                    end = it[TimeEntryTable.end],
                    day = it[TimeEntryTable.day]
                )
            }
    }
}
