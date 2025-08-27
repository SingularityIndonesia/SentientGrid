package ui.screen.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeScreenViewModel : ViewModel() {
    val state: StateFlow<HomeScreenState>
        field = MutableStateFlow(HomeScreenState())

    suspend fun onIntent(intent: HomeScreenIntent) {
        when (intent) {
            HomeScreenIntent.ToggleOpenDrawer -> onToggleOpenDrawer()
            HomeScreenIntent.GoToDashboard -> onGoToDashboard()
            HomeScreenIntent.GoToFleets -> onGoToFleets()
            HomeScreenIntent.GoToLogs -> onGoToLogs()
            HomeScreenIntent.GoToThings -> onGoToThings()
        }
    }

    private fun onToggleOpenDrawer() {
        state.update {
            it.copy(isShowDrawer = !it.isShowDrawer)
        }
    }

    private fun onGoToDashboard() {
        state.update {
            it.copy(
                mainTab = MainTab.DASHBOARD
            )
        }
    }

    private fun onGoToFleets() {
        state.update {
            it.copy(
                mainTab = MainTab.FLEETS
            )
        }
    }

    private fun onGoToLogs() {
        state.update {
            it.copy(
                mainTab = MainTab.LOGS
            )
        }
    }

    private fun onGoToThings() {
        state.update {
            it.copy(
                mainTab = MainTab.THINGS
            )
        }
    }
}