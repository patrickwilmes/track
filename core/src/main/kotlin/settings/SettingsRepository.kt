package settings

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object SettingsTable : Table(name = "settings") {
    val id = integer("id").autoIncrement()
    val trackTimeOnWeekend = bool(name = "track_time_on_weekend")

    override val primaryKey = PrimaryKey(id)
}

fun saveSettings(settings: Settings) {
    transaction {
        if (SettingsTable.selectAll().count() == 0L) {
            SettingsTable.insert {
                it[trackTimeOnWeekend] = settings.trackTimeOnWeekend
            }
        } else {
            val id = SettingsTable.selectAll().first()[SettingsTable.id]
            SettingsTable.update({ SettingsTable.id eq id }) {
                it[trackTimeOnWeekend] = settings.trackTimeOnWeekend
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

private fun ResultRow.toSettings() = Settings(this[SettingsTable.trackTimeOnWeekend])
