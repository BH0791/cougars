package fr.hamtec.data

import fr.hamtec.utils.UUIDSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Team(
    //*cause problème
    @Serializable(with = UUIDSerializer::class)
    val id: Int,        //*cause problème
    val data: TeamData,
    val name: String,
)

@Serializable
data class TeamData(
    val name: String,
    val country: String,
)

/**
 * Explications
 * Mais pourquoi ne pas l'avoir fait que sur la class Team?
 * C’est une bonne question ! Annoter directement la classe
 * `Team` avec `@Serializable(with = UUIDSerializer::class)`
 * ne fonctionne pas parce que cette annotation appliquerait le serializer `UUIDSerializer` à **toute la
 * classe** `Team`. En d'autres termes, cela signifierait que l'intégralité de l'objet `Team` serait géré
 * par ce serializer spécifique, plutôt que seulement le champ `id`.
 *
 * Cependant, voici pourquoi ça ne marche pas comme vous le suggérez :
 * 1. **Serializeur global pour toute la classe** :
 *    L'annotation `@Serializable(with = UUIDSerializer::class)` sur la classe `Team` indiquerait que cette
 *    classe entière utilise le serializeur `UUIDSerializer`, ce qui n'est pas l'intention ici. Vous voulez
 *    simplement gérer `id` de manière personnalisée, tout en laissant Kotlin Serialization traiter les autres
 *    champs (`data`, par exemple) de manière standard.
 *
 * 2. **Granularité du serializeur** :
 *    Kotlin Serialization permet d'appliquer des serializeurs à des champs spécifiques, comme vous l'avez
 *    fait avec `id`. Cela vous donne une granularité et une flexibilité pour ne pas imposer de serializeur
 *    personnalisé à des champs qui n'en ont pas besoin.
 *
 * 3. **Compatibilité des autres champs** :
 *    En annotant uniquement `id`, vous laissez Kotlin Serialization gérer naturellement le champ `data`
 *    (et ses sous-champs `name` et `country`). Cela évite des problèmes, car ces champs n'ont pas besoin
 *    d'un traitement spécial.
 *
 * En résumé, l'annotation `@Serializable(with = UUIDSerializer::class)` ne peut pas être appliquée à toute
 * la classe `Team` parce que ce serait mal interprété par Kotlin Serialization : on dirait à la bibliothèque
 * d'utiliser ce serializer pour la *classe entière* (ce qui inclurait potentiellement tous les champs de manière
 * incorrecte).
 *
 * C'est comme si vous vouliez une recette spéciale uniquement pour un ingrédient (l'`id` dans ce cas), sans
 * changer la façon de cuisiner le reste du plat. 😄 Vous voyez la subtilité ?
 */