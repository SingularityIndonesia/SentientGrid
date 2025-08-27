package ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.roundToIntRect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import font.resources.Res
import font.resources.ic_add
import font.resources.ic_search
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.BurgerMenu
import ui.component.Drawer
import ui.component.FAB
import ui.pane.DashboardPane
import ui.pane.FleetsPane
import ui.pane.LogsPane
import ui.pane.ThingsPane
import utils.pxToDp

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val viewModel = viewModel { HomeScreenViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val drawerRect = remember { mutableStateOf(IntRect.Zero) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Console/${state.mainTab.name.lowercase().replaceFirstChar { it.titlecase() }}",
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    BurgerMenu {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.ToggleOpenDrawer)
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            when (state.mainTab) {
                MainTab.DASHBOARD -> {
                    FAB(
                        "Search",
                        Res.drawable.ic_search
                    ) {

                    }
                }

                MainTab.THINGS -> {
                    FAB(
                        "Add New Thing",
                        Res.drawable.ic_add
                    ) {

                    }
                }

                MainTab.FLEETS -> {
                    FAB(
                        "Add New Fleet",
                        Res.drawable.ic_add
                    ) {

                    }
                }

                MainTab.LOGS -> {}
            }
        }
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = state.isShowDrawer,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it }
            ) {
                Drawer(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(vertical = 8.dp)
                        .padding(start = 8.dp)
                        .width(240.dp)
                        .onGloballyPositioned {
                            drawerRect.value = it.boundsInRoot().roundToIntRect()
                        },
                    onGoToDashboard = {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.GoToDashboard)
                        }
                    },
                    onGoToThings = {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.GoToThings)
                        }
                    },
                    onGoToFleets = {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.GoToFleets)
                        }
                    },
                    onGoToLogs = {
                        scope.launch {
                            viewModel.onIntent(HomeScreenIntent.GoToLogs)
                        }
                    }
                )
            }

            when (state.mainTab) {
                MainTab.DASHBOARD -> DashboardPane(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(start = drawerRect.value.topRight.x.pxToDp())
                        .fillMaxSize()
                )

                MainTab.THINGS -> ThingsPane(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(start = drawerRect.value.topRight.x.pxToDp())
                        .fillMaxSize()
                )

                MainTab.FLEETS -> FleetsPane(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(start = drawerRect.value.topRight.x.pxToDp())
                        .fillMaxSize()
                )

                MainTab.LOGS -> LogsPane(
                    modifier = Modifier
                        .padding(contentPadding)
                        .padding(start = drawerRect.value.topRight.x.pxToDp())
                        .fillMaxSize()
                )
            }
        }
    }
}
