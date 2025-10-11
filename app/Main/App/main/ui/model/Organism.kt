package ui.model

import kotlin.random.Random

data class Organism(
    val id: String,
    val name: String?,
    val status: List<Status>?,
) {
    data class Status(
        val name: String,
        val value: Number
    )
}

val dummyOrganism = (0 until 100).map {
    Organism(
        id = "$it",
        name = "Organism $it",
        status = listOf(
            Organism.Status(
                name = "LAT",
                value = Random.nextDouble(3000.0) - 1500.0
            ),
            Organism.Status(
                name = "LNG",
                value = Random.nextDouble(3000.0) - 1500.0
            ),
        )
    )
}