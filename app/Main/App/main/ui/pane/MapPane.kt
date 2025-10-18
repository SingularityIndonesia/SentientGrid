package ui.pane

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import designsystem.`24dp`
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import ui.component.SimpleIndicator
import ui.component.SimpleOrganism
import ui.component.SimpleStatus
import ui.model.Organism
import utils.onZoom
import utils.toOffsetSymmetric
import utils.tracePointer


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapPane(
    modifier: Modifier = Modifier,
    state: MapPaneState = remember { MapPaneState() },
    organism: DrawScope.(Organism, Offset) -> Unit = SimpleOrganism,
    status: DrawScope.(Organism, Offset, TextMeasurer) -> Unit = SimpleStatus,
    updateIndicator: DrawScope.(Pair<Organism, Organism>) -> Unit = SimpleIndicator
) {
    val scope = rememberCoroutineScope()
    val textMeasurer = rememberTextMeasurer()
    val updatedOrganism = state.updatedOrganism

    Canvas(
        modifier = modifier
            // trace pointer
            .tracePointer { state.pointerPosition.value = it }
            // detect zoom
            .onZoom { state.magnification.value *= it }
    ) {
        OrganismMapLayer(state) { organism, center ->
            organism(organism, center)
        }
        StatusLayer(state) { organism, center ->
            status(organism, center, textMeasurer)
        }

        // region update indicator
        // audit by position shall within the canvas
        val validUpdatedOrganism = run {
            // fixme: boilerplate
            // fixme: heavy canvas calculation
            val offsetTolerance = `24dp`.toOffsetSymmetric()
            val canvasRect = size.toRect()
                // apply rectangle tolerance for item filtering
                .let {
                    Rect(
                        it.topLeft - offsetTolerance,
                        it.bottomRight + offsetTolerance
                    )
                }

            val updatedOrganismPositions = updatedOrganism.map {
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
                    val offset = Offset(lat, lng) + this@Canvas.center
                    offset
                }

                it to organismCenter
            }

            updatedOrganismPositions
                .filter {
                    it.second != null && it.second!! in canvasRect
                }
                .map {
                    it.first
                }
        }

        validUpdatedOrganism.forEach { record ->
            updateIndicator(record)
            scope.launch {
                delay(1000)
                ensureActive()
                state.onUpdateConsumed(record)
            }
        }
        // endregion
    }
}

// fixme: heavy canvas calculation
context(drawScope: DrawScope)
private fun OrganismMapLayer(
    state: MapPaneState,
    organism: DrawScope.(Organism, Offset) -> Unit
) {
    val canvasCenter = drawScope.center
    val offsetTolerance = `24dp`.toOffsetSymmetric()
    val canvasRect = drawScope.size.toRect()
        // apply rectangle tolerance for item filtering
        .let {
            Rect(
                it.topLeft - offsetTolerance,
                it.bottomRight + offsetTolerance
            )
        }

    val positions = state.organismPositions
        // apply center pivot
        .map {
            it.first to it.second?.plus(canvasCenter)
        }
        // ignore items that isn't visible or exceed canvas boundaries
        .filter {
            it.second != null && it.second!! in canvasRect
        }

    positions.forEach { position ->
        requireNotNull(position.second) { return@forEach }
        organism(drawScope, position.first, position.second!!)
    }
}

// fixme: heavy canvas calculation
context(drawScope: DrawScope)
private fun StatusLayer(
    state: MapPaneState,
    onHovered: DrawScope.(Organism, Offset) -> Unit
) {
    val canvasCenter = drawScope.center
    val pointerPosition = state.pointerPosition.value
    requireNotNull(pointerPosition) { return }

    val interactionRectProto = with(drawScope) { 10.dp.toPx() }
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
    // with(drawScope) {
    //     hoverAbleArea.forEach {
    //         drawRect(
    //             color = Color.Cyan.copy(alpha = .5f),
    //             topLeft = it.second.topLeft,
    //             size = it.second.size
    //         )
    //     }
    // }

    val hoveredOrganism = hoverAbleArea
        .firstOrNull { pointerPosition in it.second }

    requireNotNull(hoveredOrganism) { return }

    // draw black shades covering whole screen
    with(drawScope) {
        drawRect(
            topLeft = Offset.Zero,
            size = this.size,
            color = Color.Black.copy(alpha = .3f)
        )
    }

    onHovered.invoke(drawScope, hoveredOrganism.first, hoveredOrganism.second.center)
}