import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

fun setupDatabase() {
    val datasource = "jdbc:sqlite:data.db"
    // todo - db should be placed in AppData or equal in production and in the wd in dev mode
    Database.connect(datasource, "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE

    val flyway = Flyway.configure().dataSource(datasource, null, null).load()
    flyway.migrate()
}
