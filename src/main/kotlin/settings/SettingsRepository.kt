/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package settings

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import settings.SettingsTable.hoursPerWeek
import settings.SettingsTable.remoteServerAddress
import settings.SettingsTable.storeDataRemote
import settings.SettingsTable.trackTimeOnWeekend
import java.time.Duration

object SettingsTable : Table(name = "settings") {
    val id = integer("id").autoIncrement()
    val trackTimeOnWeekend = bool(name = "track_time_on_weekend")
    val hoursPerWeek = integer(name = "hours_per_week")
    val storeDataRemote = bool(name = "store_data_remote")
    val remoteServerAddress = text(name = "remote_server_address")

    override val primaryKey = PrimaryKey(id)
}

fun saveSettings(settings: Settings) {
    transaction {
        if (SettingsTable.selectAll().count() == 0L) {
            SettingsTable.insert {
                it[trackTimeOnWeekend] = settings.trackTimeOnWeekend
                it[hoursPerWeek] = settings.hoursPerWeek.toSeconds().toInt()
                it[storeDataRemote] = settings.storeDataRemote
                it[remoteServerAddress] = settings.remoteServerAddress
            }
        } else {
            val id = SettingsTable.selectAll().first()[SettingsTable.id]
            SettingsTable.update({ SettingsTable.id eq id }) {
                it[trackTimeOnWeekend] = settings.trackTimeOnWeekend
                it[hoursPerWeek] = settings.hoursPerWeek.toSeconds().toInt()
                it[storeDataRemote] = settings.storeDataRemote
                it[remoteServerAddress] = settings.remoteServerAddress
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
    trackTimeOnWeekend = this[trackTimeOnWeekend],
    hoursPerWeek = Duration.ofSeconds((if (this[hoursPerWeek] == 0) 40 else this[hoursPerWeek]).toLong()),
    storeDataRemote = this[storeDataRemote],
    remoteServerAddress = this[remoteServerAddress],
)
