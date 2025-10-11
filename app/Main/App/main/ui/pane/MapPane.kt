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
    modifier: Modifier = Modifier,
    state: MapPaneState = remember { MapPaneState() }
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            // trace pointer
            .tracePointer { state.pointerPosition.value = it }
            // detect zoom
            .onZoom { state.magnification.value *= it }
    ) {
        OrganismMapLayer(state)
        StatusLayer(state, textMeasurer)
    }
}

private fun DrawScope.OrganismMapLayer(state: MapPaneState) {
    val organismRects = state.organismRects(this)

    organismRects.forEach { organismRect ->
        val rect = organismRect.second
        requireNotNull(rect) { return@forEach }

        drawRoundRect(
            color = Color.Black,
            size = rect.size,
            cornerRadius = CornerRadius(10f, 10f),
            topLeft = rect.topLeft
        )
    }
}

private fun DrawScope.StatusLayer(state: MapPaneState, textMeasurer: TextMeasurer) {
    val pointerPosition = state.pointerPosition.value
    requireNotNull(pointerPosition) { return }

    // fixme: recreation
    val organismRects = state.organismRects(this)
    val hoveredOrganism = organismRects.filter {
        it.second != null && pointerPosition in it.second!!
    }
    if (hoveredOrganism.isNotEmpty()) {
        drawRect(
            topLeft = Offset.Zero,
            size = this.size,
            color = Color.Black.copy(alpha = .8f)
        )
    }

    hoveredOrganism.forEach {
        val rect = it.second!!
        val status = it.first.status.orEmpty()
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
}