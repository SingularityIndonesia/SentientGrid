package ui.pane

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    organism: DrawScope.(Organism, Rect) -> Unit = { _, rect ->
        drawRoundRect(
            color = Color.Black,
            size = rect.size,
            cornerRadius = CornerRadius(4f, 4f),
            topLeft = rect.topLeft
        )
    },
    status: DrawScope.(Organism, Rect, TextMeasurer) -> Unit = { organism, rect, textMeasurer ->
        val status = organism.status.orEmpty()

        status.forEachIndexed { index, status ->
            val topRight = rect.topRight + Offset(24.dp.toPx(), (index - 1) * 32.dp.toPx())
            val fontSize = 10f
            val lineHeight = 14f

            drawText(
                textMeasurer = textMeasurer,
                text = "${status.name}: ${status.value}",
                topLeft = topRight,
                style = TextStyle(
                    color = Color.White,
                    fontSize = TextUnit(fontSize, type = TextUnitType.Sp),
                    lineHeight = TextUnit(lineHeight, type = TextUnitType.Sp)
                )
            )

            val startLine = rect.centerRight
            val firstJoint = topRight + Offset(-4.dp.toPx(), 18.dp.toPx())

            drawLine(
                start = startLine,
                end = firstJoint,
                color = Color.White,
                strokeWidth = 2.dp.toPx()
            )

            drawLine(
                start = firstJoint,
                end = firstJoint + Offset(50.dp.toPx(), 0f),
                color = Color.White,
                strokeWidth = 2.dp.toPx()
            )

            scale(2f, 2f, rect.center) {
                drawRoundRect(
                    color = Color.Red,
                    size = rect.size,
                    cornerRadius = CornerRadius(4f, 4f),
                    topLeft = rect.topLeft
                )
            }
        }
    }
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