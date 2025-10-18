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

val SimpleOrganism: DrawScope.(Organism, Offset) -> Unit = { _, center ->
    val cornerRadius = `4dp`.toPx()
    val size = simpleOrganismSize

    // debug coordinate guide
    // drawCircle(
    //     color = Color.Red,
    //     center = center,
    //     radius = 10.dp.toPx()
    // )

    drawRoundRect(
        color = Color.Black,
        size = simpleOrganismSize,
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        topLeft = center - size.center
    )
}