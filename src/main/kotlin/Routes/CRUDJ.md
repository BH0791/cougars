Allons-y pas à pas, détaillons chaque élément de ton code pour qu'il soit clair comme de l'eau de roche avant de plonger dans la jointure à droite.

### Décorticage du code
Voici ton code original :

```kotlin
get("/jointuresSuite") {
    transaction {
        val result = (Teams innerJoin Players)
            .select { Teams.name.isNotNull() and (Teams.name like "Fra%") }
            .map { row ->
                Pair(
                    row[Players.id],
                    row[Players.firstName] to row[Teams.name]
                )
            }

        if (result.isNotEmpty()) {
            val firstPlayerId = result[0].first
            println("Premier joueur ID : $firstPlayerId")
        } else {
            println("Aucun résultat trouvé.")
        }
    }
}
```

#### 1. **Route `/jointuresSuite` :**
- Il s'agit d'un endpoint qui traite une requête GET. Lorsqu'il est appelé, il effectue une transaction sur la base de données.

#### 2. **`transaction` :**
- Fournit un contexte sûr pour effectuer des requêtes en base de données avec Exposed.

#### 3. **`Teams innerJoin Players` :**
- Effectue une jointure interne (INNER JOIN) entre les tables `Teams` et `Players`.

#### 4. **`select {}` :**
- Ici, tu filtres pour inclure uniquement les lignes où :
    - `Teams.name` n'est pas nul (`isNotNull()`).
    - `Teams.name` commence par "Fra" (`like "Fra%"`).

#### 5. **`map` :**
- Chaque ligne retournée par la requête est mappée en un `Pair` :
    - La première valeur du `Pair` est `Players.id`.
    - La deuxième valeur est une autre paire contenant `Players.firstName` et `Teams.name`.

#### 6. **Conditions :**
- Si le résultat contient des données (`result.isNotEmpty()`), on récupère le premier ID de joueur.
- Sinon, un message indique qu'aucun résultat n’a été trouvé.

---

### Transition vers une "jointure à droite" (RIGHT JOIN)
En Exposed, pour effectuer une jointure à droite, tu peux utiliser `rightJoin` au lieu de `innerJoin`. Cela inclura toutes les lignes de la table de droite, même si aucune correspondance n'est trouvée dans la table de gauche. Voici comment adapter ton code :

```kotlin
get("/jointuresSuite") {
    transaction {
        val result = (Teams rightJoin Players)
            .select(Teams.name, Players.id, Players.firstName)
            .where { Teams.name.isNotNull() and (Teams.name like "Fra%") }
            .map { row ->
                Pair(
                    row[Players.id],
                    row[Players.firstName] to row[Teams.name]
                )
            }

        if (result.isNotEmpty()) {
            val firstPlayerId = result[0].first
            println("Premier joueur ID : $firstPlayerId")
        } else {
            println("Aucun résultat trouvé.")
        }
    }
}
```

---

### Résumé des changements pour la "jointure à droite" :
1. **Passage à `rightJoin`:**
    - Remplace `innerJoin` par `rightJoin` pour inclure toutes les lignes de `Players`, même si `Teams` ne contient pas de correspondance.

2. **Modification dans `select`:**
    - Spécifie explicitement les colonnes à inclure dans les résultats (`Teams.name`, `Players.id`, `Players.firstName`).

3. **Conditions avec `where`:**
    - Filtrage des résultats après la jointure pour respecter les conditions (le nom d'équipe commence par "Fra").
