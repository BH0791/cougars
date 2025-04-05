La classe **ResultRow** est au cœur de la façon dont **Exposed** représente chaque ligne renvoyée par une requête SQL. Elle permet d'accéder facilement aux colonnes de la table et de manipuler les données.

Voici quelques points essentiels sur **ResultRow** et son lien avec **Table** :

---

### **1. Fonctionnement de ResultRow** :
- Lorsque tu fais une sélection avec `Teams.selectAll()`, cela retourne un **ResultSet** de la base de données.
- Chaque ligne de ce **ResultSet** est encapsulée dans un **ResultRow**, ce qui te permet de travailler avec les données de façon typée et sécurisée.

Par exemple :
```kotlin
val teams: List<ResultRow> = Teams.selectAll().toList()

for (row in teams) {
    println("Nom : ${row[Teams.name]}, Responsable : ${row[Teams.firstName]}")
}
```

---

### **2. Utilisation des colonnes avec ResultRow** :
**ResultRow** utilise les colonnes définies dans l'objet `Table` pour accéder aux valeurs :

```kotlin
row[Teams.name] // Retourne la valeur de la colonne "name" pour cette ligne.
row[Teams.firstName] // Retourne la valeur de la colonne "firstName".
```

Cela signifie que chaque colonne définie dans ta table `Teams` devient accessible comme une propriété typée grâce à Exposed.

---

### **3. Relation avec la classe Table** :
La classe `Table` définit la structure de la table en base de données. Elle est liée à **ResultRow** en tant que modèle pour interpréter les résultats d'une requête.

Dans ton cas, l'objet `Teams` est une instance de `Table`, et ses colonnes (`name`, `firstName`) sont utilisées pour accéder aux données des lignes via **ResultRow**.

---

### Exemple complet :
Voici une utilisation complète avec des sélections et l'accès aux **ResultRow** :
```kotlin
val teams: List<ResultRow> = Teams.selectAll().toList()
for (row in teams) {
    val id = row[Teams.id]
    val name = row[Teams.name]
    val firstName = row[Teams.firstName]
    println("ID: $id | Nom: $name | Responsable: $firstName")
}
```

---

### Points importants :
- **Null Safety** : Si une colonne est définie comme nullable (`varchar("name", length).nullable()`), pense à vérifier les nulls.
- **Conversion** : Tu peux transformer chaque `ResultRow` en un objet Kotlin (DTO) pour simplifier la manipulation :
   ```kotlin
   data class Team(val id: Int, val name: String?, val firstName: String?)

   val teams: List<Team> = Teams.selectAll().map {
       Team(
           id = it[Teams.id],
           name = it[Teams.name],
           firstName = it[Teams.firstName]
       )
   }
   ```

---

En résumé, **ResultRow** est ton outil clé pour interpréter les résultats d'une requête SQL dans Exposed, et il s'appuie directement sur la définition de ta table. Tu peux l'utiliser pour accéder, transformer, et afficher les données de façon structurée et typée. ! 😊🚀✨