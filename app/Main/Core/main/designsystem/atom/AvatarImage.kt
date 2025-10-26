package designsystem.atom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.ImagePainter
import designsystem.DarkThemePalette
import font.resources.Res
import font.resources.compose_multiplatform
import model.Image
import model.particle.ImageType
import model.particle.LatLng
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AvatarImage(
    initialName: String,
    modifier: Modifier = Modifier
) {
    AvatarImageContainer(modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            HeadlineMediumText(
                text = initialName,
            )
        }
    }
}

@Composable
fun AvatarImage(
    image: Image,
    modifier: Modifier = Modifier,
    placeHolder: (@Composable (Modifier) -> Unit) = { },
    errorPlaceHolder: (@Composable (Modifier) -> Unit) = { },
) {
    val showLoading = remember { mutableStateOf(true) }
    val showError = remember { mutableStateOf(false) }

    AvatarImageContainer(
        modifier = modifier
    ) {
        when (image.type) {
            ImageType.Painter -> {
                val image = image.model as? ImagePainter

                // no loading required
                showLoading.value = false

                check(image != null) {
                    showError.value = true
                    return@AvatarImageContainer
                }

                androidx.compose.foundation.Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = image,
                    contentDescription = null
                )
            }

            ImageType.Bitmap -> {
                val image = image.model as? ImageBitmap

                // no loading required
                showLoading.value = false

                check(image != null) {
                    showError.value = true
                    return@AvatarImageContainer
                }

                androidx.compose.foundation.Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    bitmap = image,
                    contentDescription = null
                )
            }

            ImageType.DrawableResID -> {
                val res = image.model as? DrawableResource

                // no loading required
                showLoading.value = false

                check(res != null) {
                    showError.value = true
                    return@AvatarImageContainer
                }

                androidx.compose.foundation.Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(res),
                    contentDescription = null
                )
            }

            ImageType.Url -> {
                val url = image.model as? String

                check(url != null) {
                    showLoading.value = false
                    showError.value = true
                    return@AvatarImageContainer
                }

                AsyncImage(
                    model = url,
                    contentDescription = "AvatarImage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onLoading = {
                        showLoading.value = true
                    },
                    onSuccess = {
                        showLoading.value = false
                    },
                    onError = {
                        showLoading.value = false
                        showError.value = true
                    }
                )
            }
        }

        if (showLoading.value) {
            placeHolder.invoke(modifier)

            CircularProgress(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
            )
        }

        if (showError.value) {
            errorPlaceHolder.invoke(modifier)
        }
    }
}

@Composable
private fun AvatarImageContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = CircleShape,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            Box(modifier = Modifier.fillMaxSize(), content = content)
        }
    }
}

@Preview
@Composable
private fun PreviewInitial() {
    MaterialTheme(
        DarkThemePalette
    ) {
        AvatarImage(
            initialName = "XX",
            modifier = Modifier.size(64.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewImageUrl() {
    MaterialTheme(
        DarkThemePalette
    ) {
        AvatarImage(
            image = Image(
                model = Res.drawable.compose_multiplatform,
                type = ImageType.DrawableResID,
                ratio = 1f,
                desc = null,
                timeSignature = 0L,
                locationSignature = LatLng(0.0, 0.0)
            ),
            modifier = Modifier.size(64.dp)
        )
    }
}