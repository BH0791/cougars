package fr.hamtec.FileHeadlog

import io.ktor.server.application.*
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

/**
/  Enregistrer les en-têtes de la requête et les fichiers utilisés
 */

val rootDir = Paths.get("").toAbsolutePath().toString()
val logger = LoggerFactory.getLogger("HTTPLogger")

fun logHeaders(context: ApplicationCall) {
    context.request.headers.forEach { header, value ->
        context.application.environment.log.info("***** Request Header: $header - $value")
    }
}
fun logResponseHeaders(context: ApplicationCall) {
    context.response.headers.allValues().forEach { header, value ->
        context.application.environment.log.info("***** Response Header: $header - $value")
    }
}
fun logDirectoryStructure(directoryPath: String) {
    val logger = LoggerFactory.getLogger("DirectoryLogger")
    val rootDir = Paths.get(directoryPath)
    println("Mes fichiers...")
    Files.walk(rootDir).use { paths ->
        paths.filter { Files.isRegularFile(it) || Files.isDirectory(it) }
            .forEach { path ->
                val attr = Files.readAttributes(path, BasicFileAttributes::class.java)
                if(attr.isDirectory) {
                    logger.info("Dossier: ${path.toAbsolutePath()}")
                } else {
                    logger.info("Fichier: ${path.toAbsolutePath()}")
                }
            }
    }
    println()
}

fun logResourcesDirectory() {
    logDirectoryStructure("src/main/resources")
}

fun logRacineDirectory() {
    logDirectoryStructure("src/main/kotlin")
}