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
        val frameRef = Offset.Zero + center
        val pointerPosition = state.pointerPosition.value

        // draw organism
        val organism = state.organism
        val organismSize = Size(10.dp.toPx(), 10.dp.toPx())
            .div(state.magnification.value)
        val organismRects = organism.map { organism ->
            val magnification = state.magnification.value

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

        organismRects.forEach { organismRect ->
            val rect = organismRect.second
            requireNotNull(rect) { return@forEach }

            if (pointerPosition != null && pointerPosition in rect) {
                scale(2f, 2f, rect.center) {
                    drawRect(
                        color = Color.Blue,
                        size = rect.size,
                        topLeft = rect.topLeft
                    )
                }
            } else {
                drawRect(
                    color = Color.Black,
                    size = rect.size,
                    topLeft = rect.topLeft
                )
            }
        }
    }
}