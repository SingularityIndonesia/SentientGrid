package utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp

context(drawScope: DrawScope)
fun Dp.toSizeSymmetric(): Size {
    val px = with(drawScope) { this@toSizeSymmetric.toPx() }
    return Size(px, px)
}