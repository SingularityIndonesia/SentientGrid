import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ui.navigation.DeepLinkHandler

sealed class AppIntent {
    class DeepLinkNavigate(val deepLinkUri: String) : AppIntent()
}

@Composable
fun IntentHandlerEffect(intent: AppIntent?, deepLinkHandler: DeepLinkHandler, onHandled: (AppIntent) -> Unit) {
    // Handle Intent
    LaunchedEffect(intent, deepLinkHandler) {
        check(intent != null) { return@LaunchedEffect }

        when (intent) {
            is AppIntent.DeepLinkNavigate -> {
                deepLinkHandler.handleDeepLink(intent.deepLinkUri)
            }
        }

        onHandled.invoke(intent)
    }
}