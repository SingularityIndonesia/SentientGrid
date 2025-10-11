package ui.pane

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import ui.model.Organism
import utils.onZoom
import utils.tracePointer


class MapPaneState {
    val organism = mutableStateListOf<Organism>()
    val magnification = mutableStateOf(1f)
    val pointerPosition = mutableStateOf<Offset?>(null)

    fun organismRects(drawScope: DrawScope): List<Pair<Organism, Rect?>> {
        return with(drawScope) {
            val frameRef = Offset.Zero + center

            val organism = organism
            val organismSize = Size(10.dp.toPx(), 10.dp.toPx())
                .div(magnification.value)

            organism.map { organism ->
                val magnification = magnification.value

                val lat = organism.status?.firstOrNull { status -> status.name == "LAT" }?.value?.toDouble()
                    ?.times(magnification)
                    ?.toFloat()

                val lng = organism.status?.firstOrNull { status -> status.name == "LNG" }?.value?.toDouble()
                    ?.times(magnification)
                    ?.toFloat()

                // no position provided, cannot draw
                requireNotNull(lat) { return@map organism to null }
                requireNotNull(lng) { return@map organism to null }

                val topLeft = frameRef + Offset(lat, lng)
                val bottomRight = organismSize.toRect().bottomRight

                organism to Rect(topLeft, topLeft + bottomRight)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapPane(
    state: MapPaneState = remember { MapPaneState() }
) {
    Canvas(
        modifier = Modifier.fillMaxSize()
            // trace pointer
            .tracePointer { state.pointerPosition.value = it }
            // detect zoom
            .onZoom { state.magnification.value *= it }
    ) {
        val pointerPosition = state.pointerPosition.value
        val organismRects = state.organismRects(this)

        // layer 1, organism map
        organismRects.forEach { organismRect ->
            val rect = organismRect.second
            requireNotNull(rect) { return@forEach }

            drawRect(
                color = Color.Black,
                size = rect.size,
                topLeft = rect.topLeft
            )
        }

        // layer 2, status view
        requireNotNull(pointerPosition) { return@Canvas }
        organismRects.filter {
            it.second != null && pointerPosition in it.second!!
        }.map {
            val rect = it.second!!
            scale(2f, 2f, rect.center) {
                drawRect(
                    color = Color.Blue,
                    size = rect.size,
                    topLeft = rect.topLeft
                )
            }
        }
    }
}
