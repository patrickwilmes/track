import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import time.TimeEntryTable
import java.sql.Connection

private val tables = setOf(TimeEntryTable)

fun setupDatabase(rebuildDatabase: Boolean = false) {
    // todo - db should be placed in AppData or equal in production and in the wd in dev mode
    Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE

    transaction {
        if (rebuildDatabase) {
            tables.forEach { SchemaUtils.drop(it) }
        }
        tables.forEach { SchemaUtils.create(it) }
    }
}
