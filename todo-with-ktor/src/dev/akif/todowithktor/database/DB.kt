package dev.akif.todowithktor.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

class DB(val url: String) {
    private val dataSource: DataSource =
        HikariDataSource(
            HikariConfig().apply {
                driverClassName = "org.h2.Driver"
                jdbcUrl         = "jdbc:h2:$url;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE"
                validate()
            }
        )

    fun init() {
        Database.connect(dataSource)
        Flyway
            .configure()
            .dataSource(dataSource)
            .locations("filesystem:./resources/db/migrations")
            .outOfOrder(false)
            .load()
            .migrate()
    }
}
