package ui.screen.home

sealed class HomeScreenIntent {
    data object ToggleOpenDrawer : HomeScreenIntent()
    data object GoToDashboard : HomeScreenIntent()
    data object GoToThings : HomeScreenIntent()
    data object GoToFleets : HomeScreenIntent()
    data object GoToLogs : HomeScreenIntent()
}