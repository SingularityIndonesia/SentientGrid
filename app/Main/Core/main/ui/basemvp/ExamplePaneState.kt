package ui.basemvp

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf

class ExamplePaneState {
    val emailInputBuffer = mutableStateOf("")
    val emailInputError = mutableStateOf("")
    val isEmailInputError = derivedStateOf { emailInputError.value.isNotBlank() }

    val passwordInputBuffer = mutableStateOf("")
    val passwordInputError = mutableStateOf("")
    val isPasswordInputError = derivedStateOf { passwordInputError.value.isNotBlank() }

    val errorMessage = mutableStateOf("")
    val isErrorDialogShown = mutableStateOf(false)
}