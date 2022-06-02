/*
 * Copyright (c) 2022, Patrick Wilmes <patrick.wilmes@bit-lake.com>
 *
 * SPDX-License-Identifier: BSD-2-Clause
 */
package db

import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

enum class Mode {
    Dev, Prod
}

var AppMode = Mode.Prod

fun setupDatabase() {
    val databaseName = if (AppMode == Mode.Dev)
        "data.db"
    else {
        "${System.getProperty("user.home")}/data.db"
    }
    val datasource = "jdbc:sqlite:$databaseName"

    Database.connect(datasource, "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE

    val flyway = Flyway.configure().dataSource(datasource, null, null).load()
    flyway.migrate()
}
