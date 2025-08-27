package utils

import androidx.compose.runtime.Composable

@Composable
expect fun OnBackHandler(bloc: () -> Unit)