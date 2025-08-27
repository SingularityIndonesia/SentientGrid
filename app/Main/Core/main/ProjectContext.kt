import androidx.compose.runtime.staticCompositionLocalOf

data class ProjectContext(
    val versionName: String = "NULL",
    val versionCode: String = "NULL",
    val hostName: String = "somewhere.whatever.com",
    val webHostUrl: String = "https://somewhere.whatever.com/",
    val deepLinkHostName: String = "singularityapp",
    val deepLinkHostUrl: String = "singularityapp://"
)

val LocalProjectContext = staticCompositionLocalOf { ProjectContext() }