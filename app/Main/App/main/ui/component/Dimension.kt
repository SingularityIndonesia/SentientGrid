package ui.component

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val `2dp` = 2.dp
val `4dp` = 4.dp
val `10dp` = 10.dp
val `20dp` = 20.dp
val `24dp` = 24.dp

val `4sp` = 4.sp
val `6sp` = 6.sp
val `14sp` = 14.sp
val `18sp` = 18.sp
val `32sp` = 32.sp
val `50sp` = 50.sp

context(drawScope: DrawScope)
fun Dp.toOffsetSymmetric(): Offset {
    val px = with(drawScope) { this@toOffsetSymmetric.toPx() }
    return Offset(px, px)
}

context(drawScope: DrawScope)
fun Dp.toSizeSymmetric(): Size {
    val px = with(drawScope) { this@toSizeSymmetric.toPx() }
    return Size(px, px)
}