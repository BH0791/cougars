# InnerJoint
```kotlin
val results = val results: List<resultRow> = (Teams innerJoin Players)
 .select(Players.id, Players.firstName, Teams.name)
 .where { Teams.name like "FRA%" }
 .toList()
 ```
 Voici un exemple de ce que `results` pourrait contenir en fonction des données présentes dans tes tables `Teams` et `Players` :

**Table Teams**:  
| id | name       |  
|----|------------|  
| 1  | France     |  
| 2  | Francfort  |  
| 3  | Espagne    |  

**Table Players**:  
| id | first_name | team_id |  
|----|------------|---------|  
| 1  | Antoine    | 1       |  
| 2  | Pierre     | 2       |  
| 3  | Sergio     | NULL    |  

### Résultat attendu :
Étant donné que la condition dans ta requête est `Teams.name like "FRA%"`, seules les équipes dont le nom commence par "FRA" seront inclues dans la jointure. Cela signifie les équipes `France` et `Francfort`. La jointure ne prendra en compte que les joueurs associés à ces équipes.

**Résultat final de `results`** :  
| Players.id | Players.first_name | Teams.name   |  
|------------|--------------------|--------------|  
| 1          | Antoine            | France       |  
| 2          | Pierre             | Francfort    |  

### Explications :
1. **Jointure** : La requête relie les tables `Teams` et `Players` sur `Teams.id = Players.team_id`.
2. **Filtrage** : La condition `Teams.name like "FRA%"` exclut l'équipe `Espagne` et tout joueur associé à des équipes ne correspondant pas au filtre.
3. **Projection des colonnes** : Seules les colonnes `Players.id`, `Players.first_name`, et `Teams.name` sont incluses dans le résultat.

```kotlin
val results = val results: List<resultRow> = (Teams innerJoin Players)
 .select(Players.id, Players.firstName, Teams.name)
 .where { Teams.name like "FRA%" }
 .toList()
 ```

Dans Exposed, tu peux utiliser la méthode `join` pour spécifier les paramètres comme la table à joindre (`otherTable`), le type de jointure (`joinType`), et les colonnes sur lesquelles la condition de jointure repose (`onColumn` et `otherColumn`). Voici une explication détaillée et un exemple pour illustrer cela :

### Syntaxe et exemple
Avec ta suggestion, cela pourrait ressembler à ceci :

```kotlin
val results = Teams.join(
    otherTable = Players,
    joinType = JoinType.INNER,
    onColumn = Teams.id,
    otherColumn = Players.teamId
).select(Players.id, Players.firstName, Teams.name)
 .where { Teams.name like "FRA%" }
 .toList()
```

### Explications des paramètres :
1. **otherTable** :
   - C'est la table avec laquelle tu veux effectuer la jointure (dans ton cas, `Players`).

2. **joinType** :
   - Définit le type de jointure à effectuer. Ici, `JoinType.INNER` spécifie une jointure interne.
   - D'autres options disponibles incluent `LEFT`, `RIGHT`, et `FULL` selon tes besoins.

3. **onColumn** et **otherColumn** :
   - Ces paramètres définissent les colonnes utilisées pour établir la relation entre les deux tables. Dans l'exemple :
     - `onColumn` correspond à `Teams.id` (la clé primaire de la table `Teams`).
     - `otherColumn` correspond à `Players.teamId` (la clé étrangère dans la table `Players` qui fait référence à `Teams.id`).

4. **select et where** :
   - Une fois la jointure établie, tu peux sélectionner les colonnes nécessaires et appliquer des conditions pour filtrer les résultats (ici, `Teams.name like "FRA%"`).

### Comparaison avec `innerJoin` :
L'approche avec `join` donne davantage de contrôle et est plus explicite, surtout si tu veux manipuler les jointures dynamiquement ou travailler avec différents types de jointures dans un seul projet. Cela peut être utile dans des scénarios complexes.

# JoinType.RIGHT
Un **JoinType.RIGHT**, ou une **jointure droite**, récupère toutes les lignes de la table droite (celle avec laquelle tu fais la jointure) et les lignes correspondantes de la table gauche, selon la condition de jointure. Si une ligne de la table droite n'a pas de correspondance dans la table gauche, les colonnes de la table gauche seront remplies avec `NULL`.

### Exemple avec tes tables `Teams` et `Players`
Imaginons ces données :

**Table Teams** :  
| id | name       |  
|----|------------|  
| 1  | France     |  
| 2  | Brésil     |  

**Table Players** :  
| id | first_name | team_id |  
|----|------------|---------|  
| 1  | Antoine    | 1       |  
| 2  | Neymar     | 2       |  
| 3  | Sergio     | NULL    |  

Si on effectue une jointure droite avec Exposed :
```kotlin
val results = Teams.join(
    otherTable = Players,
    joinType = JoinType.RIGHT,
    onColumn = Teams.id,
    otherColumn = Players.teamId
).select(Players.id, Players.firstName, Teams.name).toList()
```

### Résultat attendu :
| Players.id | Players.first_name | Teams.name |  
|------------|--------------------|------------|  
| 1          | Antoine            | France     |  
| 2          | Neymar             | Brésil     |  
| 3          | Sergio             | NULL       |  

### Explication :
1. **Table droite (Players)** :
   - Toutes les lignes de la table `Players` sont incluses, qu'elles aient une correspondance ou non dans la table `Teams`.

2. **Table gauche (Teams)** :
   - Les colonnes `Teams.name` sont remplies uniquement si une correspondance existe avec la table `Players`. Pour les joueurs comme `Sergio`, où `team_id` est `NULL`, aucune équipe n'est trouvée, donc la colonne `Teams.name` sera également `NULL`.

### Utilité d'une jointure droite :
Ce type de jointure est utile dans les cas où la priorité est donnée à la table droite, et où tu veux inclure toutes ses données, même si la relation avec la table gauche est incomplète.

# JoinType.LEFT
Un **JoinType.LEFT**, ou une **jointure gauche**, inclut toutes les lignes de la table de gauche (première table spécifiée dans la jointure) et uniquement les lignes correspondantes de la table droite. Si une ligne de la table gauche n’a pas de correspondance dans la table droite, les colonnes de la table droite sont remplies avec `NULL`.

### Exemple avec tes tables `Teams` et `Players`

**Table Teams** :  
| id | name       |  
|----|------------|  
| 1  | France     |  
| 2  | Brésil     |  
| 3  | Espagne    |  

**Table Players** :  
| id | first_name | team_id |  
|----|------------|---------|  
| 1  | Antoine    | 1       |  
| 2  | Neymar     | 2       |  
| 3  | Sergio     | NULL    |  

Avec une jointure gauche en Exposed :
```kotlin
val results = Teams.join(
    otherTable = Players,
    joinType = JoinType.LEFT,
    onColumn = Teams.id,
    otherColumn = Players.teamId
).select(Players.id, Players.firstName, Teams.name).toList()
```

### Résultat attendu :
| Teams.name | Players.id | Players.first_name |  
|------------|------------|--------------------|  
| France     | 1          | Antoine            |  
| Brésil     | 2          | Neymar             |  
| Espagne    | NULL       | NULL               |  

### Explication :
1. **Table gauche (Teams)** :
   - Toutes les lignes de la table `Teams` sont incluses dans le résultat, indépendamment de la présence ou non de correspondances dans la table `Players`.

2. **Correspondance avec la table droite (Players)** :
   - Pour les équipes `France` et `Brésil`, une correspondance est trouvée dans `Players`, donc les colonnes de `Players` sont remplies normalement.
   - Pour l'équipe `Espagne`, aucune correspondance n’est trouvée dans `Players` (`Players.team_id` ne contient pas `3`), donc les colonnes de `Players` sont remplies avec `NULL`.

### Utilité d'une jointure gauche :
Une jointure gauche est idéale lorsque tu veux t'assurer que toutes les données de la table de gauche apparaissent dans les résultats, même si la relation avec la table droite est incomplète. Cela permet, par exemple, de lister toutes les équipes, même celles qui n’ont pas encore de joueurs associés.

# JoinType.FULL
Un **JoinType.FULL**, aussi appelé **jointure complète** ou **Full Outer Join**, récupère toutes les lignes des deux tables, qu'elles aient ou non une correspondance. Lorsque des lignes de l'une des tables n'ont pas de correspondance dans l'autre, les colonnes de l'autre table sont remplies avec `NULL`.

### Exemple avec tes tables `Teams` et `Players`

**Table Teams** :  
| id | name       |  
|----|------------|  
| 1  | France     |  
| 2  | Brésil     |  
| 3  | Espagne    |  

**Table Players** :  
| id | first_name | team_id |  
|----|------------|---------|  
| 1  | Antoine    | 1       |  
| 2  | Neymar     | 2       |  
| 3  | Sergio     | NULL    |  

Avec une jointure complète en Exposed :
```kotlin
val results = Teams.join(
    otherTable = Players,
    joinType = JoinType.FULL,
    onColumn = Teams.id,
    otherColumn = Players.teamId
).select(Players.id, Players.firstName, Teams.name).toList()
```

### Résultat attendu :
| Teams.name | Players.id | Players.first_name |  
|------------|------------|--------------------|  
| France     | 1          | Antoine            |  
| Brésil     | 2          | Neymar             |  
| Espagne    | NULL       | NULL               |  
| NULL       | 3          | Sergio             |  

### Explication :
1. **Inclusion des deux tables** :
   - Toutes les lignes de `Teams` et de `Players` sont incluses dans le résultat.
   - Si une équipe n'a aucun joueur associé (comme `Espagne`), les colonnes de `Players` sont remplies avec `NULL`.
   - Si un joueur n'a aucune équipe associée (`team_id` est `NULL`, comme `Sergio`), les colonnes de `Teams` sont remplies avec `NULL`.

2. **Utilité d'une jointure complète** :
   - Ce type de jointure est pratique lorsque tu veux voir l’ensemble des données des deux tables, y compris celles qui ne sont pas directement reliées entre elles. Par exemple, pour un rapport complet des équipes et des joueurs, qu'ils soient liés ou non.

### Points à considérer :
- Une **Full Outer Join** peut produire des résultats volumineux si tes tables contiennent beaucoup de lignes avec des correspondances partielles ou des données non reliées.
- Pour optimiser les performances, il est utile d'ajouter des conditions (comme un `WHERE`) pour limiter les résultats lorsque cela est pertinent.

Pour résumer, la **Inner Join** est effectivement la plus utilisée dans les bases de données relationnelles. Voici pourquoi elle est si populaire :

### Pourquoi la **Inner Join** est souvent utilisée :
1. **Cibler les relations valides** :
   - Elle ne retourne que les lignes où il existe une correspondance entre les deux tables, ce qui permet de travailler uniquement sur les données pertinentes liées.

2. **Optimisée pour les performances** :
   - Comparée à d'autres types de jointures, comme les **Left Join** ou **Full Join**, l’Inner Join ne renvoie pas les lignes inutiles (celles sans correspondance), ce qui peut améliorer les performances dans de nombreux cas.

3. **Scénarios courants** :
   - Elle est idéale pour relier des entités associées, comme `Players` et `Teams`, `Commandes` et `Produits`, ou encore `Employés` et `Départements`.

4. **Syntaxe simple et compréhensible** :
   - La syntaxe d’une **Inner Join** est intuitive et facile à intégrer dans des requêtes complexes, ce qui explique son adoption massive.

### Quand utiliser d'autres jointures ?
- La **Left Join** est privilégiée quand tu veux inclure toutes les lignes d'une table, même si elles n’ont pas de correspondance.
- La **Full Join** est utile pour obtenir une vue complète des deux tables, qu'il y ait correspondance ou non.
- La **Right Join**, bien qu'elle ait une logique similaire à la Left Join, est moins utilisée car elle donne priorité à la table droite (ce qui est rare en pratique).

En gros, pour les besoins classiques de gestion et d’analyse des données, la **Inner Join** reste le pilier des jointures en SQL.
---
# HTML-DSL
Utiliser `HTML DSL` pour afficher un tableau dans un `respondHtml()` rendrait la réponse bien plus structurée et visuellement lisible. Voici comment on peut adapter ton code pour générer un tableau HTML avec les résultats de la jointure :

### Exemple avec `respondHtml`
```kotlin
get("/jointuresInnerExplicite") {
    withContext(Dispatchers.IO) {
        val result = transaction {
            Teams.join(
                otherTable = Players,
                joinType = JoinType.INNER,
                onColumn = Teams.id,
                otherColumn = Players.teamId
            )
            .select(Players.id, Players.firstName, Teams.name)
            .toList()
        }

        // Génération de la réponse HTML avec HTML DSL
        call.respondHtml {
            head {
                title { +"Résultats de la Jointure" }
            }
            body {
                h1 { +"Résultats de la Jointure INNER entre Teams et Players" }
                if (result.isNotEmpty()) {
                    table {
                        style = "border-collapse: collapse; width: 100%; text-align: left;"
                        tr {
                            th { +"Player ID" }
                            th { +"First Name" }
                            th { +"Team Name" }
                        }
                        result.forEach { row ->
                            tr {
                                td { +row[Players.id].toString() }
                                td { +row[Players.firstName] }
                                td { +row[Teams.name] }
                            }
                        }
                    }
                } else {
                    p { +"Aucun résultat trouvé." }
                }
            }
        }
    }
}
```

### Explications étape par étape :
1. **`respondHtml {}`** :
   - Utilise le DSL HTML intégré à Ktor pour créer du contenu HTML directement dans le code. Cela rend le résultat visuellement agréable.

2. **Structure HTML** :
   - `head` : Définis le titre de la page HTML.
   - `body` : Contient le contenu principal, comme le titre et le tableau des résultats.

3. **Tableau HTML** :
   - Une balise `<table>` est utilisée pour afficher les données sous forme de tableau.
   - Les balises `<th>` définissent les en-têtes de colonnes : `Player ID`, `First Name` et `Team Name`.
   - Les données récupérées de la jointure (`result.forEach`) sont insérées ligne par ligne avec `<tr>` pour les lignes et `<td>` pour les cellules.

4. **Affichage conditionnel** :
   - Si la liste `result` est vide, un paragraphe (`<p>`) indique qu'aucun résultat n'a été trouvé.

5. **Style minimal** :
   - Une règle CSS simple est ajoutée à la table pour la rendre plus agréable à lire (`style = "border-collapse: collapse; width: 100%; text-align: left;"`).

### Résultat attendu :
Si des données existent, un tableau lisible est renvoyé sous forme de HTML dans la réponse HTTP. Sinon, le message "Aucun résultat trouvé." s'affiche.
