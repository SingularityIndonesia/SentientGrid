package ui.pane

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ui.model.Organism

class MapPaneState {
    private val mutex = Mutex()

    val magnification = mutableStateOf(1f)
    val pointerPosition = mutableStateOf<Offset?>(null)

    val organisms = mutableStateListOf<Organism>()
    val organismPositions: List<Pair<Organism, Offset?>>
        get() {
            // fixme
            //val magnification = magnification.value

            return organisms.map { organism ->
                val lat = organism.status?.firstOrNull { status -> status.name == "LAT" }?.value?.toDouble()
                    // fixme
                    //?.times(magnification)
                    ?.toFloat()

                val lng = organism.status?.firstOrNull { status -> status.name == "LNG" }?.value?.toDouble()
                    // fixme
                    //?.times(magnification)
                    ?.toFloat()

                // no position provided, cannot draw
                requireNotNull(lat) { return@map organism to null }
                requireNotNull(lng) { return@map organism to null }

                // fixme: adjust this to latlng magnitude later
                val offset = Offset(lat, lng)

                organism to offset
            }
        }

    val updatedOrganism = mutableStateListOf<Pair<Organism, Organism>>()
    suspend fun update(organism: Organism) {
        // fixme: hyper memory allocation
        mutex.withLock {
            val index = organisms.indexOfFirst { it.id == organism.id }
            val old = organisms[index]
            val head = organisms.take(index)
            val tail = organisms.takeLast(organisms.size - index - 1)

            val newList = head + organism + tail
            organisms.clear()
            organisms.addAll(newList)
            updatedOrganism.add(old to organism)
        }
    }

    fun onUpdateConsumed(record: Pair<Organism, Organism>) {
        updatedOrganism.remove(record)
    }
}