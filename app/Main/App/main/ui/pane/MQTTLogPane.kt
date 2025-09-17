package ui.pane

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MQTTLogPane(
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val log = remember { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        Text(
            text = "MQTT Log",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.size(24.dp))
        Text(
            text = log.value
        )
    }
}