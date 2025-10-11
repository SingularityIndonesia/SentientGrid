package utils

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.showPointer(offset: Offset?): Modifier {
    return drawWithContent {
        drawContent()

        // draw pointer
        if (offset != null)
            drawCircle(
                color = Color.Red,
                radius = 10f,
                center = offset
            )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.tracePointer(
    bloc: (Offset?) -> Unit
): Modifier {
    return onPointerEvent(PointerEventType.Move) {
        bloc.invoke(it.changes.first().position)
    }.onPointerEvent(PointerEventType.Exit) {
        bloc.invoke(null)
    }
}

fun Modifier.onZoom(
    bloc: (zoom: Float) -> Unit
): Modifier {
    return pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            println("alsdnladnladn $pan $zoom")
            bloc.invoke(zoom)
        }
    }
}