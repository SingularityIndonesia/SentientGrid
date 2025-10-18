package ui.component

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import designsystem.`10dp`
import designsystem.`4dp`
import ui.model.Organism
import utils.toSizeSymmetric

private val DrawScope.simpleOrganismSize
    get() = `10dp`.toSizeSymmetric()

val SimpleIndicator: DrawScope.(Pair<Organism, Organism>) -> Unit = { data ->
    val cornerRadius = `4dp`.toPx()
    val organism = data.first

    val organismCenter = run {
        val lat = organism.status?.firstOrNull { status -> status.name == "LAT" }?.value?.toDouble()
            // fixme
            //?.times(magnification)
            ?.toFloat()

        val lng = organism.status?.firstOrNull { status -> status.name == "LNG" }?.value?.toDouble()
            // fixme
            //?.times(magnification)
            ?.toFloat()

        // no position provided, cannot draw
        requireNotNull(lat) { return@run null }
        requireNotNull(lng) { return@run null }

        // fixme: adjust this to latlng magnitude later
        val offset = Offset(lat, lng) + this.center
        offset
    }

    if (organismCenter != null)
        drawRoundRect(
            color = Color.Green,
            size = simpleOrganismSize,
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            topLeft = organismCenter - simpleOrganismSize.center
        )
}