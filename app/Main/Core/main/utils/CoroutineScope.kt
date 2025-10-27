package utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive

suspend inline fun <R> CoroutineScope.resumeEnsureActive(bloc: suspend () -> R): R {
    val result = bloc.invoke()
    ensureActive()
    return result
}