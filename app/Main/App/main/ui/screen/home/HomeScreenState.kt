package ui.screen.home

data class HomeScreenState(
    val title: String = "Console/Dashboard",
    val isShowDrawer: Boolean = true,
    val mainTab: MainTab = MainTab.DASHBOARD
)