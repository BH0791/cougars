package fr.hamtec

/**
 * Fonction classique avec lambda
 */
fun process(block: (String) -> Unit) {
    block("Fonction classique avec lambda")
}

/**
 * Lambda avec récepteur
 */
fun process2(block: String.() -> Unit) {
    "Lambda avec récepteur".block()
}

fun String.bold(action: StringBuilder.() -> Unit): String {
    val builder = StringBuilder()
    builder.action() // `this` est le StringBuilder ici
    return "<b>${builder}</b>"
}

fun appliqueFonctionExtention() {
    process { value ->
        println(value) // "Hello"
    }
    process2 {
        println(this) // "Hello", et `this` est implicite
    }

    val result = "Important".bold {
        append(this) // this` est implicite ici, mais référencé pour clarté
    }
    println(result) // <b>Important</b>
}

fun applShoppingList() {
    val list = shoppingList {
        add("Pommes")
        add("Bananes")
    }
    println(list) // [Pommes, Bananes]
}

fun shoppingList(block: MutableList<String>.() -> Unit): List<String> {
    val list = mutableListOf<String>()
    list.block()
    return list
}

fun repeatAction(times: Int, block: () -> Unit) {
    for(i in 1..times) {
        block() // Appelle le code défini entre {}
    }
}