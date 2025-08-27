package utils

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend inline fun <T, R> T.runCatching(
    context: CoroutineContext,
    crossinline block: suspend T.() -> R
): Result<R> {
    return withContext(context) {
        runCatching {
            block()
        }
    }
}