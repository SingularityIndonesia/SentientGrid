package stdextra

import kotlin.test.assertFails
import kotlin.test.assertTrue

fun <T> assertSuccess(bloc: () -> T) {
    assertTrue { runCatching { bloc.invoke() }.isSuccess }
}

infix fun <T> T.shouldBe(expected: T) {
    assertTrue { this == expected }
}

sealed class ProcessStatus
data object Fail : ProcessStatus()
data object Success : ProcessStatus()

infix fun <T> (() -> T).must(status: ProcessStatus) {
    when (status) {
        Fail -> assertFails { this.invoke() }
        Success -> assertSuccess { this.invoke() }
    }
}