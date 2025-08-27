package utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Number.px: Dp
    @Composable get() {
        val density = LocalDensity.current
        return this.toFloat().dp / density.density
    }