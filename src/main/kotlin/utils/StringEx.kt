package fr.hamtec.utils

import java.util.*

fun String.nullIfBlank(): String? =
    ifBlank {
        null
    }

fun String.toUUID(): UUID = UUID.fromString(this)
