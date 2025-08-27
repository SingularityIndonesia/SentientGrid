package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Number.pxToDp(): Dp {
    return (this.toFloat() / LocalDensity.current.density).dp
}