package ui.component

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ui.model.Organism

val SimpleStatus: DrawScope.(Organism, Rect, TextMeasurer) -> Unit = { organism, rect, textMeasurer ->
    val status = organism.status.orEmpty()

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