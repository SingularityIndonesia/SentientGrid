package ui.screen.world

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ui.model.dummyOrganism
import ui.pane.MapPane
import ui.pane.MapPaneState


@Composable
fun WorldScreen() {
    // data
    val organism = remember { dummyOrganism }

    // component state
    val mapPaneState = remember { MapPaneState() }

    // init state
    LaunchedEffect(organism) {
        mapPaneState.organism.addAll(organism)
    }

    WorldScaffold {
        MapPane(
            modifier = Modifier.fillMaxSize(),
            state = mapPaneState
        )
    }
}