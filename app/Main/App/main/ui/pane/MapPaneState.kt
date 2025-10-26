package ui.pane

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import designsystem.`24dp`
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
                val offset = Offset(
                    organism.location.lat.toFloat(),
                    organism.location.lng.toFloat()
                )

                organism to offset
            }
        }

    private val updatedOrganism = mutableStateListOf<Pair<Organism, Organism>>()

    @OptIn(ExperimentalStdlibApi::class)
    @Composable
    fun updatedOrganism(): State<List<Pair<Organism, Organism>>> = snapshotStateOf {
        require(canvasSize.value != Size.Zero) { return@snapshotStateOf emptyList() }
        require(updatedOrganism.isNotEmpty()) { return@snapshotStateOf emptyList() }

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

        val updatedOrganismPositions = updatedOrganism.map {
            val organismCenter = run {
                val location = it.second.location
                val offset = Offset(
                    location.lat.toFloat(),
                    location.lng.toFloat()
                ) + canvasRect.center
                offset
            }

            it to organismCenter
        }

        updatedOrganismPositions
            .filter {
                it.second in canvasRectWithTolerance
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
            updatedOrganism.add(old to organism)
        }
    }

    fun onUpdateConsumed(record: Pair<Organism, Organism>) {
        updatedOrganism.remove(record)
    }
}


@Composable
fun rememberMapPaneState(): MapPaneState {
    val density = LocalDensity.current
    return remember { MapPaneState(density) }
}