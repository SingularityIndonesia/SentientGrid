package ui.basemvi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ExamplePane(
    presenter: ExamplePresenter = viewModel { ExamplePresenter() }
) {
    val state = presenter.state

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(horizontal = 24.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(24.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = state.emailInputBuffer.value,
                isError = state.isEmailInputError.value,
                supportingText = if (state.isEmailInputError.value) {
                    { Text(state.emailInputError.value) }
                } else null,
                label = {
                    Text("Email")
                },
                onValueChange = {
                    state.emailInputBuffer.value = it
                }
            )
            TextField(
                value = state.passwordInputBuffer.value,
                isError = state.isPasswordInputError.value,
                supportingText = if (state.isPasswordInputError.value) {
                    { Text(state.passwordInputError.value) }
                } else null,
                label = {
                    Text("Password")
                },
                onValueChange = {
                    state.passwordInputBuffer.value = it
                }
            )
            Button(
                onClick = {
                    presenter.submitLogin()
                }
            ) {
                Text("Login")
            }
        }

        if (state.isErrorDialogShown.value)
            PopUpErrorDialog(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp),
                message = state.errorMessage.value,
                onClose = { presenter.closeErrorDialog() }
            )
    }
}

@Composable
fun PopUpErrorDialog(
    modifier: Modifier = Modifier,
    message: String,
    onClose: () -> Unit
) {
    Column(
        modifier = modifier
            .width(300.dp)
            .shadow(elevation = 4.dp, RoundedCornerShape(16.dp))
            .background(
                MaterialTheme.colorScheme.errorContainer,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Error: $message")
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onClose
            ) {
                Text("Close")
            }
        }
    }
}