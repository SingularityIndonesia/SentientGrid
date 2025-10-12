package ui.screen.world

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import ui.model.Organism
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
        mapPaneState.organisms.addAll(organism)
    }

    LaunchedEffect(Unit) {
        delay(3000)
        val org = mapPaneState.organisms
            .first()

        mapPaneState.update(
            org.copy(
                status = org.status
                    ?.filterNot { it.name == "TMP" }
                    ?.plus(Organism.Status("TMP", 0f))
            )
        )
    }

    WorldScaffold {
        MapPane(
            modifier = Modifier.fillMaxSize(),
            state = mapPaneState
        )
    }
}