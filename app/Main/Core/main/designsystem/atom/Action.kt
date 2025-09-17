package designsystem.atom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import font.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalIconButtonColor = staticCompositionLocalOf<IconButtonColors?> { null }

@Preview
@Composable
fun OptionsMenuSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_more_horz,
        contentDescription = "OptionsMenu",
        onClick = onClick
    )
}

@Preview
@Composable
fun EditSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_edit,
        contentDescription = "Edit",
        onClick = onClick
    )
}

@Preview
@Composable
fun AddSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_add,
        contentDescription = "Edit",
        onClick = onClick
    )
}

@Preview
@Composable
fun DeleteSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_delete,
        contentDescription = "Dekete",
        onClick = onClick
    )
}

@Preview
@Composable
fun CloseSearchSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_close,
        contentDescription = "CloseSearch",
        onClick = onClick
    )
}

@Preview
@Composable
fun AddMediaSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_add_photo_alternate,
        contentDescription = "CloseSearch",
        onClick = onClick
    )
}

@Preview
@Composable
fun AddMedia(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    ActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.add_image,
        contentDescription = "CloseSearch",
        onClick = onClick
    )
}

@Preview
@Composable
fun ClearSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_close,
        contentDescription = "Clear",
        onClick = onClick
    )
}

@Preview
@Composable
fun NextSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_next,
        contentDescription = "Next",
        onClick = onClick
    )
}

@Preview
@Composable
fun BackSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_back,
        contentDescription = "Back",
        onClick = onClick
    )
}

@Preview
@Composable
fun Back(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    ActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.go_previous,
        contentDescription = "Back",
        onClick = onClick
    )
}

@Preview
@Composable
fun SignOutSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_logout,
        contentDescription = "SignOut",
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        onClick = onClick
    )
}

@Composable
fun SearchSymbolic(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SymbolicActionIcon(
        modifier = modifier,
        drawRes = Res.drawable.ic_search,
        contentDescription = "Search",
        onClick = onClick
    )
}

@Composable
private fun SymbolicActionIcon(
    modifier: Modifier = Modifier,
    drawRes: DrawableResource,
    contentDescription: String,
    colors: IconButtonColors = LocalIconButtonColor.current
        ?: IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(drawRes),
            contentDescription = contentDescription
        )
    }
}

@Composable
private fun ActionIcon(
    modifier: Modifier = Modifier,
    drawRes: DrawableResource,
    contentDescription: String,
    colors: IconButtonColors = LocalIconButtonColor.current
        ?: IconButtonDefaults.iconButtonColors(),
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors
    ) {
        Image(
            modifier = Modifier.size(28.dp),
            painter = painterResource(drawRes),
            contentDescription = contentDescription
        )
    }
}