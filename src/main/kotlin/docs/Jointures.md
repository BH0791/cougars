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
