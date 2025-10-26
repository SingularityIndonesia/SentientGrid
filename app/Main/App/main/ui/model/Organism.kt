package ui.model

import model.particle.LatLng
import kotlin.random.Random

data class Organism(
    val id: String,
    val name: String?,
    val location: LatLng,
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
        location = LatLng(
            Random.nextDouble(3000.0) - 1500.0,
            Random.nextDouble(3000.0) - 1500.0
        ),
        status = listOf(
            Organism.Status(
                name = "TMP",
                value = Random.nextDouble(60.0) + 40.0
            ),
        ),
    )
}