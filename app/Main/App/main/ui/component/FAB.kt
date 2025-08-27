package ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun FAB(
    text: String,
    iconRes: DrawableResource,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        modifier = Modifier.border(
            BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
        ),
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(0.dp),
        text = {
            Text(text)
        },
        icon = {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null
            )
        },
        onClick = onClick
    )
}