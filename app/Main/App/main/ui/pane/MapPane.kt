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