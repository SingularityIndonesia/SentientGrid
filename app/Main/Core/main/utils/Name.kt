package utils

fun String.initialName(): String {
    return replace(".", " ")
        .replace(",", " ")
        .replace("  ", " ")
        .replace("   ", " ")
        .split(" ")
        .let {
            if (it.size > 1) it.take(2) else it.take(1)
        }
        .joinToString("") { it.first().toString().uppercase() }
}