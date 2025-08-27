package utils

import androidx.compose.ui.focus.FocusRequester
import kotlin.time.Duration

suspend fun FocusRequester.requestFocus(timeout: Duration): Boolean {
    return temporaryLoop(timeout = timeout) {
        requestFocus()
    }.getOrElse {
        println("Error: FocusRequester.requestFocus ${it.message?.takeIf { it.isNotBlank() } ?: it.cause}")
        false
    }
}