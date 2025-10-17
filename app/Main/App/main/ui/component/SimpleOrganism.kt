package ui.component

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import ui.model.Organism

val SimpleOrganism: DrawScope.(Organism, Rect) -> Unit = { _, rect ->
    val cornerRadius = 4.dp.toPx()

    drawRoundRect(
        color = Color.Black,
        size = rect.size,
        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
        topLeft = rect.topLeft
    )
}