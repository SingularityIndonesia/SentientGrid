package utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

context(density: Density)
fun Dp.toOffsetSymmetric(): Offset {
    val px = with(density) { this@toOffsetSymmetric.toPx() }
    return Offset(px, px)
}