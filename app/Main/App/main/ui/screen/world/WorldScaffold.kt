package ui.screen.world

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

class WorldScaffoldState {
    val contentPadding = mutableStateOf(PaddingValues(0.dp))
    val isStatusPaneVisible = mutableStateOf(true)

    internal fun setStatusPaneSize(intSize: IntSize) {
        // todo
    }

    internal fun setStatusPanePosition(offset: Offset) {
        // todo
    }
}

@Composable
fun WorldScaffold(
    state: WorldScaffoldState = remember { WorldScaffoldState() },
    statusPane: @Composable RowScope.() -> Unit = {},
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // at lowest layer
        content(state.contentPadding.value)

        AnimatedVisibility(
            visible = state.isStatusPaneVisible.value
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .onSizeChanged {
                        state.setStatusPaneSize(it)
                    }
                    .onGloballyPositioned {
                        state.setStatusPanePosition(it.positionInParent())
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                statusPane()
            }
        }
    }
}