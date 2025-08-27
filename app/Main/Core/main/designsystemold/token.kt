package designsystemold

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * note: please write the system token `as is`, event if it sounds redundant.
 * ex: color/text/text-primary into colorTextTextPrimary.
 */
@Deprecated(
    "Do not call this directly!",
    replaceWith = ReplaceWith(
        "DesignToken.current",
        "core.ui.designsystem.DesignToken",
    ),
)
data class SystemToken(
    val color: ColorScheme,
    val typography: Typography = Typography()
)


val DesignToken = staticCompositionLocalOf<SystemToken> {
    error("not provided")
}
