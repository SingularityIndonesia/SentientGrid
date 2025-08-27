package ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Drawer(
    modifier: Modifier,
    onGoToDashboard: (() -> Unit)? = null,
    onGoToThings: (() -> Unit)? = null,
    onGoToFleets: (() -> Unit)? = null,
    onGoToLogs: (() -> Unit)? = null,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .border(
                BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface
                ),
            ),
    ) {
        onGoToDashboard?.let {
            item {
                ListItem(
                    modifier = Modifier
                        .padding(2.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
                        .clickable { it.invoke() },
                    headlineContent = {
                        Text("Dashboard")
                    }
                )
            }
        }
        onGoToThings?.let {
            item {
                ListItem(
                    modifier = Modifier
                        .padding(2.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
                        .clickable { it.invoke() },
                    headlineContent = {
                        Text("Things")
                    }
                )
            }
        }
        onGoToFleets?.let {
            item {
                ListItem(
                    modifier = Modifier
                        .padding(2.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
                        .clickable { it.invoke() },
                    headlineContent = {
                        Text("Fleets")
                    }
                )
            }
        }
        onGoToLogs?.let {
            item {
                ListItem(
                    modifier = Modifier
                        .padding(2.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
                        .clickable { it.invoke() },
                    headlineContent = {
                        Text("Logs")
                    }
                )
            }
        }
    }
}