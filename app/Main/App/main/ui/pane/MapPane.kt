package ui.pane

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import ui.component.SimpleOrganism
import ui.component.SimpleStatus
import ui.model.Organism
import utils.onZoom
import utils.tracePointer


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapPane(
    modifier: Modifier = Modifier,
    state: MapPaneState = remember { MapPaneState() },
    organism: DrawScope.(Organism, Offset) -> Unit = SimpleOrganism,
    status: DrawScope.(Organism, Offset, TextMeasurer) -> Unit = SimpleStatus
) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            // trace pointer
            .tracePointer { state.pointerPosition.value = it }
            // detect zoom
            .onZoom { state.magnification.value *= it }
    ) {
        OrganismMapLayer(state) { organism, center ->
            organism(this, organism, center)
        }
        StatusLayer(state) { organism, center ->
            status(this, organism, center, textMeasurer)
        }
    }
}

private fun DrawScope.OrganismMapLayer(state: MapPaneState, organism: DrawScope.(Organism, Offset) -> Unit) {
    val canvasCenter = center
    val positions = state.organismPositions

    positions.forEach { position ->
        val center = position.second?.plus(canvasCenter)
        requireNotNull(center) { return@forEach }
        organism(this, position.first, center)
    }
}

private fun DrawScope.StatusLayer(state: MapPaneState, onHovered: DrawScope.(Organism, Offset) -> Unit) {
    val canvasCenter = center
    val pointerPosition = state.pointerPosition.value
    requireNotNull(pointerPosition) { return }

    val interactionRectProto = 10.dp.toPx()
        .let { Rect(Offset.Zero, Offset(it, it)) }
    val interactionRectProtoCenter = interactionRectProto.center
    val hoverAbleArea = state.organismPositions
        .mapNotNull {
            requireNotNull(it.second) { return@mapNotNull null }

            val rect = interactionRectProto
                .translate(it.second!!)
                .translate(canvasCenter)
                .translate(interactionRectProtoCenter * -1f)

            it.first to rect
        }

    // debug draw hoverable area
    // hoverAbleArea.forEach {
    //     drawRect(
    //         color = Color.Cyan.copy(alpha = .5f),
    //         topLeft = it.second.topLeft,
    //         size = it.second.size
    //     )
    // }

    val hoveredOrganism = hoverAbleArea
        .firstOrNull { pointerPosition in it.second }

    requireNotNull(hoveredOrganism) { return }

    // draw black shades covering whole screen
    drawRect(
        topLeft = Offset.Zero,
        size = this.size,
        color = Color.Black.copy(alpha = .3f)
    )

    onHovered.invoke(this, hoveredOrganism.first, hoveredOrganism.second.center)
}