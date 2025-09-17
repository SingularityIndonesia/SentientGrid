package designsystem.atom

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun RatioImage(
    model: Any,
    modifier: Modifier = Modifier,
) {
    val imageAspectRatio = remember { mutableStateOf(1f) }
    AsyncImage(
        modifier = modifier
            .aspectRatio(imageAspectRatio.value),
        model = model,
        contentDescription = "Selected media",
        contentScale = ContentScale.Crop,
        onSuccess = { success ->
            val width = success.result.image.width
            val height = success.result.image.height
            imageAspectRatio.value = width / height.toFloat()
        }
    )
}