package utils

import androidx.compose.runtime.Composable

@Composable
expect fun WithPermission(vararg permissions: String, content: @Composable () -> Unit)