package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@ExperimentalStdlibApi
@Composable
fun <T> snapshotStateOf(block: () -> T): State<T> {
    return snapshotFlow(block).collectAsStateWithLifecycle(initialValue = block())
}