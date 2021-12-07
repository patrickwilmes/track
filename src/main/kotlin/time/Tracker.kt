package time

import Today
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
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

object TimeEntryTable : Table(name = "time_entry") {
    val id = integer("id").autoIncrement()
    val projectName = text("project_name")
    val description = text("description")
    val start = datetime("start")
    val end = datetime("end")
    val day = datetime("day")

    override val primaryKey = PrimaryKey(id)
}

fun deleteAll() {
    transaction {
        TimeEntryTable.deleteAll()
    }
}

fun updateTimeEntry(timeEntry: TimeEntry, newDuration: Duration) {
    val newEndTime = timeEntry.start.plusSeconds(newDuration.seconds)
    val newTimeEntry = timeEntry.copy(end = newEndTime)
    transaction {
        TimeEntryTable.update({ TimeEntryTable.id eq newTimeEntry.id!! }) { it[end] = newTimeEntry.end }
    }
}

fun saveTimeEntry(timeEntry: TimeEntry, doRound: Boolean = false) {
    if (doRound) {
        // todo - do magic
    }
    transaction {
        TimeEntryTable.insert {
            it[projectName] = timeEntry.projectName
            it[description] = timeEntry.description
            it[start] = timeEntry.start
            it[end] = timeEntry.end
            it[day] = Today.atStartOfDay()
        }
    }
}

fun deleteTimeEntryById(id: Int) {
    transaction {
        TimeEntryTable.deleteWhere { TimeEntryTable.id eq id }
    }
}

fun getTimeEntriesForDay(date: LocalDate): List<TimeEntry> {
    val start = date.with(DayOfWeek.MONDAY)
    val end = date.with(DayOfWeek.FRIDAY)
    return transaction {
        TimeEntryTable
            .select { TimeEntryTable.start.greaterEq(start.atStartOfDay()) and TimeEntryTable.end.lessEq(end.atStartOfDay().plusDays(1)) }
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
