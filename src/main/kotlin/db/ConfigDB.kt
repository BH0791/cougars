package fr.hamtec.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import fr.hamtec.FileHeadlog.logger
import fr.hamtec.data.Players
import fr.hamtec.data.Teams
import fr.hamtec.dotenv
import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

suspend fun Application.configurationDB() {
    withContext(Dispatchers.IO) {
        try {
            // Configuration de la connexion
            // liste complère au: https://github.com/brettwooldridge/HikariCP?tab=readme-ov-file#gear-configuration-knobs-baby
            val config = HikariConfig()
            config.jdbcUrl = "jdbc:postgresql://localhost:5432/teams"
            config.driverClassName = "org.postgresql.Driver"
            config.maximumPoolSize = 10
            // délai d’expiration de la connexion à 30 000 millisecondes
            config.connectionTimeout = 30000
            // délai d’inactivité à 600 000 millisecondes
            //config.idleTimeout = 600000
            // durée de vie maximale à 1 800 000 millisecondes
            //config.maxLifetime=1800000
            // Lecture dans le fichier .env
            //val dotenv = dotenv() dans Application.kt
            config.username = dotenv["DB_USERNAME"]
            config.password = dotenv["DB_PASSWORD"]

            val dataSource = HikariDataSource(config)
            Database.connect(dataSource)

            logger.info("\"Entrée dans Flyway !")
            try {
                migrateDatabase(dataSource)
                logger.info("\"Flyway migrateDatabase-ok !")
            } catch(e: FlywayException) {
                logger.error("Erreur lors des migrations Flyway : ${e.message}")
            } catch(e: SQLException) {
                logger.error("Erreur SQL : ${e.message}")
            } catch(e: Exception) {
                logger.error("Erreur inattendue : ${e.message}")
            }
//            Flyway
//                .configure()
//                .dataSource(dataSource)
//                .baselineOnMigrate(true)
//                .validateOnMigrate(true)
//                .load()
//                .migrate()

            logger.info("\"Sortie de Flyway ")
            logger.info("Migrations de la nouvelle base de données effectuées avec succès ! ")
            logger.info("\"Migrations effectuées avec succès ! ")
            transaction {
                SchemaUtils.create(Teams, Players)
            }

            logger.info("\"Connexion à la base de données effectuée avec succès ! ")

        } catch(ex: Exception) {
            logger.error("---------- Erreur de connexion à la base de données *** : ${ex.message} ")
        }
    }
}

suspend fun migrateDatabase(dataSource: HikariDataSource) {
    logger.info("\"Méthode migrateDatabase()  ! ")
    Flyway
        .configure()
        .dataSource(dataSource)
        .baselineOnMigrate(true)
        .validateOnMigrate(true)
        .load()
        .migrate()
}
