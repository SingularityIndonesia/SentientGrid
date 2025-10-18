package utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp

context(drawScope: DrawScope)
fun Dp.toOffsetSymmetric(): Offset {
    val px = with(drawScope) { this@toOffsetSymmetric.toPx() }
    return Offset(px, px)
}