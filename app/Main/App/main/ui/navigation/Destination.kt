package ui.navigation

import ProjectContext

object Route {
    // region Init
    lateinit var projectContext: ProjectContext
        private set

    val webHostUrl = projectContext.webHostUrl
    val deepLinkHostUrl = projectContext.deepLinkHostUrl

    fun setupWith(projectContext: ProjectContext): Route {
        this.projectContext = projectContext
        return this
    }
    // endregion

    // region Home
    val HomeDestination get() = "home"
    // endregion
}
