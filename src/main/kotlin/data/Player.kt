package fr.hamtec.data

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Int,
    val firstName: String,
)
