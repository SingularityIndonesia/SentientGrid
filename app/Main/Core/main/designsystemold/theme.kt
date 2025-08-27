package designsystemold

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember


@Composable
fun SingularityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = remember("$darkTheme") {
        if (darkTheme)
            DarkThemePalette
        else
            LightThemePaletteV2
    }

    val typeSystem = TypingSystem

    @Suppress("DEPRECATION")
    val systemToken = remember { SystemToken(colorScheme, typeSystem) }

    CompositionLocalProvider(
        DesignToken provides systemToken,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typeSystem,
            content = content
        )
    }
}
