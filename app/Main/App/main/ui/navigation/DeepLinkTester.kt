package ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import designsystem.atom.HeadlineMediumText
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Just launch the preview to test deeplinks.
 *
 * Important: Everytime you made changes to the navigation, you must re-install the app so the new app have the navigation you have created.
 * If you have not yet install the new app, adding launcher here is useless because the installed app version is the old one that does not have new navigation handler.
 */
@Preview
@Composable
fun DeepLinkTester() {
    val uriHandler = LocalUriHandler.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HeadlineMediumText(
            text = "DeepLink Test",
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        Button(
            onClick = {
                uriHandler.openUri("singularityapp://about")
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Launch About DeepLink")
        }
        
        Button(
            onClick = {
                uriHandler.openUri("singularityapp://security/setting")
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Launch Security Setting DeepLink")
        }
        
        Button(
            onClick = {
                uriHandler.openUri("singularityapp://help")
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Launch Help & Support DeepLink")
        }
        
        Button(
            onClick = {
                uriHandler.openUri("singularityapp://account/setting")
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Launch Account Setting DeepLink")
        }
        
        Button(
            onClick = {
                uriHandler.openUri("singularityapp://notification/setting")
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Launch Notification Setting DeepLink")
        }
    }
}
