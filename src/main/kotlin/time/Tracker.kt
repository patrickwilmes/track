package time

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime

data class TimeEntry(
    val id: Int? = null,
    val projectName: String,
    val description: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
)

object TimeEntryTable : Table(name = "time_entry") {
    val id = integer("id").autoIncrement()
    val projectName = text("project_name")
    val description = text("description")
    val start = datetime("start")
    val end = datetime("end")
    val day = datetime("day")

    override val primaryKey = PrimaryKey(id)
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
            it[day] = LocalDate.now().atStartOfDay()
        }
    }
}

fun getTimeEntriesForDay(date: LocalDate): List<TimeEntry> {
    return transaction {
        TimeEntryTable
            .select { TimeEntryTable.start.greaterEq(date.atStartOfDay()) and TimeEntryTable.end.lessEq(date.atStartOfDay().plusDays(1)) }
            .map {
                TimeEntry(
                    id = it[TimeEntryTable.id],
                    projectName = it[TimeEntryTable.projectName],
                    description = it[TimeEntryTable.description],
                    start = it[TimeEntryTable.start],
                    end = it[TimeEntryTable.end],
                )
            }
    }
}
