package utils

import androidx.compose.runtime.Composable

@Composable
actual fun WithPermission(
    vararg permissions: String,
    content: @Composable (() -> Unit)
) {
    TODO()
}