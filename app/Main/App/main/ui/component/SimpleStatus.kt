package ui.component

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.model.Organism

val SimpleStatus: DrawScope.(Organism, Offset, TextMeasurer) -> Unit = { organism, center, textMeasurer ->
    val fontSize = 6.sp.toPx()
    val lineHeight = 14.sp.toPx()
    val textStyle = TextStyle(
        color = Color.White,
        fontSize = TextUnit(fontSize, type = TextUnitType.Sp),
        lineHeight = TextUnit(lineHeight, type = TextUnitType.Sp),
        fontWeight = FontWeight.Bold
    )
    val status = organism.status.orEmpty()
    val overlayOrganismSize = 20.dp.toPx()
        .let { Size(it, it) }
    val overlayOrganismRect = overlayOrganismSize
        .toRect()
        .translate(center)
        .translate(overlayOrganismSize.center * -1f)
    val firstStatusPosition = overlayOrganismRect.topRight + Offset(24.dp.toPx(), -24.dp.toPx())

    status.forEachIndexed { index, status ->
        val statusTopRight = firstStatusPosition + Offset(0f, 32.sp.toPx() * index)
        val startLine = overlayOrganismRect.centerRight
        val lineJoint = statusTopRight + Offset(-4.sp.toPx(), 18.sp.toPx())

        drawText(
            textMeasurer = textMeasurer,
            text = "${status.name}: ${status.value}",
            topLeft = statusTopRight,
            style = textStyle
        )

        drawLine(
            start = startLine,
            end = lineJoint,
            color = Color.White,
            strokeWidth = 2.dp.toPx()
        )

        drawLine(
            start = lineJoint,
            end = lineJoint + Offset(50.sp.toPx(), 0f),
            color = Color.White,
            strokeWidth = 2.dp.toPx()
        )
    }

    drawRoundRect(
        color = Color.Red,
        size = overlayOrganismSize,
        cornerRadius = CornerRadius(4.dp.toPx()),
        topLeft = overlayOrganismRect.topLeft
    )
}