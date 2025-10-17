package ui.pane

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ui.component.SimpleOrganism
import ui.component.SimpleStatus
import ui.model.Organism
import utils.onZoom
import utils.tracePointer


class MapPaneState {
    private val mutex = Mutex()

    val organisms = mutableStateListOf<Organism>()
    val magnification = mutableStateOf(1f)
    val pointerPosition = mutableStateOf<Offset?>(null)
    val organismSize = mutableStateOf(10.dp)
    val organismPositions: List<Pair<Organism, Offset?>>
        get() {
            val magnification = magnification.value

            return organisms.map { organism ->
                val lat = organism.status?.firstOrNull { status -> status.name == "LAT" }?.value?.toDouble()
                    ?.times(magnification)
                    ?.toFloat()

                val lng = organism.status?.firstOrNull { status -> status.name == "LNG" }?.value?.toDouble()
                    ?.times(magnification)
                    ?.toFloat()

                // no position provided, cannot draw
                requireNotNull(lat) { return@map organism to null }
                requireNotNull(lng) { return@map organism to null }

                // fixme: adjust this to latlng magnitude later
                val offset = Offset(lat, lng)

                organism to offset
            }
        }

    context(drawScope: DrawScope)
    val organismRects: List<Pair<Organism, Rect?>>
        get() = with(drawScope) {
            val frameRef = center

            return organismPositions.map {
                val position = it.second
                requireNotNull(position) { return@map it.first to null }

                val organismSize = organismSize.value.toPx()
                    .let { size -> Size(size, size) }

                val rect = organismSize.toRect()
                val centeredRect = rect
                    .translate(frameRef)
                    .translate(position)
                    .translate(organismSize.center * -1f)

                it.first to centeredRect
            }
        }

    suspend fun update(organism: Organism) {
        // fixme: hyper memory allocation
        mutex.withLock {
            val index = organisms.indexOfFirst { it.id == organism.id }
            val head = organisms.take(index)
            val tail = organisms.takeLast(organisms.size - index - 1)

            val newList = head + organism + tail
            organisms.clear()
            organisms.addAll(newList)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapPane(
    modifier: Modifier = Modifier,
    state: MapPaneState = remember { MapPaneState() },
    organism: DrawScope.(Organism, Rect) -> Unit = SimpleOrganism,
    status: DrawScope.(Organism, Rect, TextMeasurer) -> Unit = SimpleStatus
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            // trace pointer
            .tracePointer { state.pointerPosition.value = it }
            // detect zoom
            .onZoom { state.magnification.value *= it }
    ) {
        OrganismMapLayer(state) { organism, rect ->
            organism(this, organism, rect)
        }
        StatusLayer(state) { organism, rect ->
            status(this, organism, rect, textMeasurer)
        }
    }
}

private fun DrawScope.OrganismMapLayer(state: MapPaneState, organism: DrawScope.(Organism, Rect) -> Unit) {
    val organismRects = state.organismRects

    organismRects.forEach { organismRect ->
        val rect = organismRect.second
        requireNotNull(rect) { return@forEach }
        organism(this, organismRect.first, rect)
    }
}

private fun DrawScope.StatusLayer(state: MapPaneState, onHovered: DrawScope.(Organism, Rect) -> Unit) {
    val pointerPosition = state.pointerPosition.value
    requireNotNull(pointerPosition) { return }

    val hoveredOrganism = state.organismRects
        .filter { it.second != null }
        .map {
            @Suppress("UNCHECKED_CAST")
            it as Pair<Organism, Rect>
        }
        .filter {
            pointerPosition in it.second
        }

    // draw black shades covering whole screen
    if (hoveredOrganism.isNotEmpty()) {
        drawRect(
            topLeft = Offset.Zero,
            size = this.size,
            color = Color.Black.copy(alpha = .8f)
        )
    }

    hoveredOrganism.forEach {
        onHovered.invoke(this, it.first, it.second)
    }
}