package designsystemold.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StorageUsageCard(
    storageUsed: String,
    totalStorage: String,
    progress: Float,
    modifier: Modifier = Modifier,
    title: String = "Storage",
    onManageClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TitleMediumText(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface
                )
                BodyMediumText(
                    text = "$storageUsed of $totalStorage",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onManageClick,
                modifier = Modifier.wrapContentWidth()
            ) {
                BodySmallText(
                    text = "Manage storage",
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun StorageUsageCardPreview() {
    MaterialTheme {
        StorageUsageCard(
            storageUsed = "8.7 GB",
            totalStorage = "15 GB",
            progress = 0.58f,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun StorageUsageCardLowUsagePreview() {
    MaterialTheme {
        StorageUsageCard(
            storageUsed = "2.3 GB",
            totalStorage = "15 GB",
            progress = 0.15f,
            modifier = Modifier.padding(16.dp)
        )
    }
}
