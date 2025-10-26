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

// fixme: heavy canvas calculation
val SimpleIndicator: DrawScope.(Pair<Organism, Organism>) -> Unit = { data ->
    val cornerRadius = `4dp`.toPx()
    val organism = data.second
    val pos = Offset(
        organism.location.lat.toFloat(),
        organism.location.lng.toFloat()
    )

    val organismCenter = pos + center

    drawRoundRect(
        color = Color.Green,
        size = simpleOrganismSize,
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        topLeft = organismCenter - simpleOrganismSize.center
    )
}