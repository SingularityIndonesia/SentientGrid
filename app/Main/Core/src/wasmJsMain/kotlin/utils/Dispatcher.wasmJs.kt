package utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun ioDispatchers(): CoroutineDispatcher {
    return Dispatchers.Default
}