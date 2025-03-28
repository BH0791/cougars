package fr.hamtec.Routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Application.configureRoutesWebSockets() {
    routing {
        webSocket("/hello1") {
            println("Nouvelle connexion WebSocket établie.")
            send(Frame.Text("HELLO")) // Envoi du message initial

            try {
                for (frame in incoming) { // Boucle pour écouter les frames entrantes
                    when (frame) {
                        is Frame.Text -> {
                            val receivedText = frame.readText()
                            println("Message reçu du client : $receivedText")
                            send(Frame.Text("Reçu : $receivedText")) // Réponse au client
                        }
                        is Frame.Close -> {
                            println("Connexion fermée par le client.")
                            close() // Fermer la session si demandée par le client
                        }
                        else -> println("Frame inconnue ou non gérée.")
                    }
                }
            } catch (e: Exception) {
                println("Erreur WebSocket : ${e.message}")
            } finally {
                println("Connexion WebSocket terminée.")
            }
        }

        webSocket("/echo")  {
            for(frame in incoming){
                val text: String = (frame as? Frame.Text)?.readText()?: continue
                if(text.trim().equals("bye", true)){
                    send(Frame.Close())
                }else{
                    send(Frame.Text(text))
                }
            }
        }
        webSocket("/hello") {
            println("Nouvelle connexion WebSocket établie.")
            send(Frame.Text("HELLO")) // Message initial

            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val message = frame.readText()
                        println("Message reçu : $message")

                        // Logique de réponse dynamique
                        val response = when (message.lowercase()) {
                            "ping" -> "pong"
                            "bonjour" -> "Salut, client !"
                            "conard" -> "Toi même!!!"
                            else -> "Reçu : $message"
                        }
                        send(Frame.Text(response)) // Réponse dynamique au client
                    }
                    is Frame.Close -> {
                        println("Connexion fermée par le client.")
                        close()
                    }
                    else -> println("Frame non gérée.")
                }
            }
        }
    }
}