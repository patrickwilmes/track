package settings

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object SettingsTable : Table(name = "settings") {
    val id = integer("id").autoIncrement()
    val trackTimeOnWeekend = bool(name = "track_time_on_weekend")
    val hoursPerWeek = integer(name = "hours_per_week")

    override val primaryKey = PrimaryKey(id)
}

fun saveSettings(settings: Settings) {
    transaction {
        if (SettingsTable.selectAll().count() == 0L) {
            SettingsTable.insert {
                it[trackTimeOnWeekend] = settings.trackTimeOnWeekend
                it[hoursPerWeek] = settings.hoursPerWeek
            }
        } else {
            val id = SettingsTable.selectAll().first()[SettingsTable.id]
            SettingsTable.update({ SettingsTable.id eq id }) {
                it[trackTimeOnWeekend] = settings.trackTimeOnWeekend
                it[hoursPerWeek] = settings.hoursPerWeek
            }
        }
    }
}

fun getSettings() = transaction {
    SettingsTable
        .selectAll()
        .firstOrNull()
        ?.toSettings() ?: Settings()
}

private fun ResultRow.toSettings() = Settings(
    trackTimeOnWeekend = this[SettingsTable.trackTimeOnWeekend],
    hoursPerWeek = if (this[SettingsTable.hoursPerWeek] == 0) 40 else this[SettingsTable.hoursPerWeek],
)
