import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import time.TimeEntryTable
import java.sql.Connection

private val tables = setOf(TimeEntryTable)

fun setupDatabase(rebuildDatabase: Boolean = false) {
    val datasource = "jdbc:sqlite:data.db"
    // todo - db should be placed in AppData or equal in production and in the wd in dev mode
    Database.connect(datasource, "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE

    val flyway = Flyway.configure().dataSource(datasource, null, null).load()
    flyway.migrate()
}
