package ui.pane

import LocalProjectContext
import ProjectContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
//import service.things.ThingsService


sealed class ThingsPaneIntent {
    data object Refresh : ThingsPaneIntent()
}

data class ThingsPaneState(
    val refreshProgressIsShown: Boolean = false,
    val things: List<ThingsDisplay> = emptyList()
)

//class ThingsPaneViewModel(
//    private val thingsService: ThingsService
//) : ViewModel() {
//
//    constructor(context: ProjectContext) : this(
//        thingsService = ThingsService(context)
//    )
//
//    val uiState: StateFlow<ThingsPaneState>
//        field = MutableStateFlow(ThingsPaneState())
//
//    suspend fun onIntent(intent: ThingsPaneIntent) {
//        when (intent) {
//            ThingsPaneIntent.Refresh -> onRefresh()
//        }
//    }
//
//    private suspend fun onRefresh() {
//        uiState.update {
//            it.copy(
//                refreshProgressIsShown = true
//            )
//        }
//        thingsService.getListOfThings()
//            .onFailure {
//                // todo: handle fail
//                println("aksdjalds $it")
//            }
//            .onSuccess {response ->
//                uiState.update {
//                    it.copy(
//                        things = response.map {
//                            ThingsDisplay(it)
//                        }
//                    )
//                }
//            }
//
//        uiState.update {
//            it.copy(
//                refreshProgressIsShown = false
//            )
//        }
//    }
//}

@Composable
fun ThingsPane(
    modifier: Modifier = Modifier,
) {
    val projectContext = LocalProjectContext.current
//    val viewModel = viewModel { ThingsPaneViewModel(projectContext) }
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    // fixme: get rid of this one off event
//    LaunchedEffect(Unit) {
//        viewModel.onIntent(ThingsPaneIntent.Refresh)
//    }
//
//    Box(
//        modifier = modifier
//            .padding(8.dp)
//            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface))
//            .fillMaxSize()
//    ) {
//        LazyColumn {
//            items(uiState.things) {
//                Text("${it.name} ${it.serialNumber} ${it.model}")
//            }
//        }
//    }
}