package ui.screen.world

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import ui.model.Organism
import ui.model.dummyOrganism
import ui.pane.MapPane
import ui.pane.MapPaneState
import kotlin.random.Random


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

    // emulate update dummy
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)

            val org = mapPaneState.organisms
                .random()

            val copy = org.copy(
                status = org.status
                    ?.filterNot { it.name == "TMP" }
                    ?.plus(Organism.Status("TMP", Random.nextDouble(60.0) + 40.0))
            )

            mapPaneState.update(copy)
        }
    }

    WorldScaffold {
        MapPane(
            modifier = Modifier.fillMaxSize(),
            state = mapPaneState
        )
    }
}