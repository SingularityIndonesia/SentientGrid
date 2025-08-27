package utils

import io.ktor.util.date.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

sealed class TemporaryLoopException : Exception() {
    data class UnknownException(override val message: String) : TemporaryLoopException()
    data class OperationFailureException(override val cause: Throwable) : TemporaryLoopException()
}

suspend fun <T> temporaryLoop(
    timeout: Duration,
    delay: Duration = 100.milliseconds,
    block: suspend () -> T
): Result<T> {
    val timeoutMillis = timeout.inWholeMilliseconds // 2 seconds max
    val delayMillis = delay.inWholeMilliseconds

    val tic = getTimeMillis()
    val toc = { getTimeMillis() - tic }
    var result: Result<T> = Result.failure(TemporaryLoopException.UnknownException("Execution not yet invoked"))

    while (coroutineContext.isActive && (toc() < timeoutMillis)) {
        runCatching { block.invoke() }
            .onFailure {
                result = Result.failure(TemporaryLoopException.OperationFailureException(it))
            }
            .onSuccess {
                result = Result.success(it)
                break
            }

        delay(delayMillis)
    }

    return result
}