package infra.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseConnection {
    @Volatile
    private var database: Database? = null
    private var dataSource: HikariDataSource? = null

    /**
     * Get database connection (initializes pool if not already done)
     * Safe to call multiple times - returns same instance
     */
    fun connection(
        driverClassName: String = "org.postgresql.Driver",
        jdbcUrl: String = "jdbc:postgresql://localhost:5432/things",
        username: String = "root",
        password: String = "root",
        maximumPoolSize: Int = 10
    ): Database {
        return database ?: synchronized(this) {
            database ?: run {
                println("Initializing database connection pool...")

                val config = HikariConfig().apply {
                    this.driverClassName = driverClassName
                    this.jdbcUrl = jdbcUrl
                    this.username = username
                    this.password = password
                    this.maximumPoolSize = maximumPoolSize
                    this.isAutoCommit = false
                    this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

                    // Connection pool settings
                    this.connectionTimeout = 30000 // 30 seconds
                    this.idleTimeout = 600000 // 10 minutes
                    this.maxLifetime = 1800000 // 30 minutes
                    this.minimumIdle = 1

                    validate()
                }

                dataSource = HikariDataSource(config)
                database = Database.connect(dataSource!!)

                // Register shutdown hook
                registerShutdownHook()

                println("Database connection pool initialized successfully")
                database!!
            }
        }
    }

    /**
     * Initialize H2 database for testing - call once per test suite
     */
    fun testConnection(): Database {
        return connection(
            driverClassName = "org.h2.Driver",
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            username = "sa",
            password = ""
        )
    }

    /**
     * Close database connection pool - call once at application shutdown
     */
    fun close() {
        synchronized(this) {
            dataSource?.let { ds ->
                println("Closing database connection pool...")
                ds.close()
                println("Database connection pool closed")
            }
            dataSource = null
            database = null
        }
    }

    /**
     * Register shutdown hook to properly close connections
     */
    private fun registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread {
            close()
        })
    }
}
