package fr.hamtec.utils
/*
-    Receveurs lambda
- Les "receivers" permettent d’utiliser une instance donnée comme contexte à l’intérieur d’une lambda.
- Concrètement, cela signifie que toutes les méthodes et propriétés de l'objet receveur sont accessibles
- sans le préfixer.

Exemple basique :
 */
class Greeter {
    fun greet(name: String) = "Hello, $name!"
}

fun useGreeter(block: Greeter.() -> Unit) {
    val greeter = Greeter()
    greeter.block()
}
//-Appel de la méthode
//*     useGreeter {
//*         println(greet("Kotlin")) // Pas besoin d'utiliser "this.greet()"
//*     }
