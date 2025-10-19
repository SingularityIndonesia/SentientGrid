package ui.pane

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import designsystem.`24dp`
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ui.model.Organism
import utils.snapshotStateOf
import utils.toOffsetSymmetric

class MapPaneState(
    private val density: Density,
) {
    private val mutex = Mutex()

    val canvasSize = mutableStateOf(Size.Zero)
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

    private val _updatedOrganism = mutableStateListOf<Pair<Organism, Organism>>()
    private val updatedOrganismResultState = mutableStateOf<List<Pair<Organism, Organism>>>(emptyList())

    @OptIn(ExperimentalStdlibApi::class)
    @Composable
    fun updatedOrganism(): State<List<Pair<Organism, Organism>>> = snapshotStateOf {
        require(canvasSize.value != Size.Zero) { return@snapshotStateOf emptyList() }
        require(_updatedOrganism.isNotEmpty()) { return@snapshotStateOf emptyList() }

        // fixme: boilerplate
        val offsetTolerance = with(density) { `24dp`.toOffsetSymmetric() }
        val canvasRect = canvasSize.value.toRect()
        val canvasRectWithTolerance = canvasRect
            // apply rectangle tolerance for item filtering
            .let {
                Rect(
                    it.topLeft - offsetTolerance,
                    it.bottomRight + offsetTolerance
                )
            }

        val updatedOrganismPositions = _updatedOrganism.map {
            val organismCenter = run {
                val lat = it.second.status?.firstOrNull { status -> status.name == "LAT" }?.value?.toDouble()
                    // fixme
                    //?.times(magnification)
                    ?.toFloat()

                val lng = it.second.status?.firstOrNull { status -> status.name == "LNG" }?.value?.toDouble()
                    // fixme
                    //?.times(magnification)
                    ?.toFloat()

                // no position provided, cannot draw
                requireNotNull(lat) { return@run null }
                requireNotNull(lng) { return@run null }

                // fixme: adjust this to latlng magnitude later
                val offset = Offset(lat, lng) + canvasRect.center
                offset
            }

            it to organismCenter
        }

        updatedOrganismPositions
            .filter {
                it.second != null && it.second!! in canvasRectWithTolerance
            }
            .map {
                it.first
            }
    }

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
            _updatedOrganism.add(old to organism)
        }
    }

    fun onUpdateConsumed(record: Pair<Organism, Organism>) {
        _updatedOrganism.remove(record)
    }
}


@Composable
fun rememberMapPaneState(): MapPaneState {
    val density = LocalDensity.current
    return remember { MapPaneState(density) }
}