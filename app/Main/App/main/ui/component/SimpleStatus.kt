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
import ui.model.Organism

val SimpleStatus: DrawScope.(Organism, Offset, TextMeasurer) -> Unit = { organism, center, textMeasurer ->
    val fontSize = `6sp`.toPx()
    val lineHeight = `14sp`.toPx()
    val textStyle = TextStyle(
        color = Color.White,
        fontSize = TextUnit(fontSize, type = TextUnitType.Sp),
        lineHeight = TextUnit(lineHeight, type = TextUnitType.Sp),
        fontWeight = FontWeight.Bold
    )
    val status = organism.status.orEmpty()
    val overlayOrganismSize = `20dp`.toPx()
        .let { Size(it, it) }
    val overlayOrganismRect = overlayOrganismSize
        .toRect()
        .translate(center)
        .translate(overlayOrganismSize.center * -1f)
    val firstStatusPosition = overlayOrganismRect.topRight + Offset(`24dp`.toPx(), -`24dp`.toPx())

    status.forEachIndexed { index, status ->
        val statusTopRight = firstStatusPosition + Offset(0f, `32sp`.toPx() * index)
        val startLine = overlayOrganismRect.centerRight
        val lineJoint = statusTopRight + Offset(-`4sp`.toPx(), `18sp`.toPx())

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
            strokeWidth = `2dp`.toPx()
        )

        drawLine(
            start = lineJoint,
            end = lineJoint + Offset(`50sp`.toPx(), 0f),
            color = Color.White,
            strokeWidth = `2dp`.toPx()
        )
    }

    drawRoundRect(
        color = Color.Red,
        size = overlayOrganismSize,
        cornerRadius = CornerRadius(`4dp`.toPx()),
        topLeft = overlayOrganismRect.topLeft
    )
}