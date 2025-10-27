package ui.basemvp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import utils.resumeEnsureActive

/**
 * Job of a presenter is capture intent from UI then proceed the intent and giving back intent result.
 */
class ExamplePresenter(
    val state: ExamplePaneState = ExamplePaneState(),
    val route: ExampleRoute = ExampleRoute(),
    val dataSource: DummyDataSource = DummyDataSource()
) : ViewModel() {

    private var submitLoginJob: Job? = null
    fun submitLogin() {
        submitLoginJob?.cancel()
        submitLoginJob = viewModelScope.launch {
            // check email procedure
            val email = state.emailInputBuffer.value
            require(email.isNotBlank()) {
                val error = IllegalStateException("Email Cannot Blank")
                state.emailInputError.value = error.message!!
                return@launch
            }

            // check password procedure
            val password = state.passwordInputBuffer.value
            require(password.isNotBlank()) {
                val error = IllegalStateException("Password Cannot Blank")
                state.passwordInputError.value = error.message!!
                return@launch
            }

            val result = resumeEnsureActive { dataSource.submitLogin(email,password) }
            result.onSuccess {
                route.goToDashboard()
            }
            result.onFailure {
                state.errorMessage.value = it.message ?: "Unknown Error"
                state.isErrorDialogShown.value = true
            }
        }
    }

    fun closeErrorDialog() {
        state.errorMessage.value = ""
        state.isErrorDialogShown.value = false
    }
}