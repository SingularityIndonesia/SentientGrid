package designsystemold

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

// Set of Material typography styles to start with
val TypingSystem @Composable get() = Typography(
    displayLarge = TextSansSerif57NormalLSmin25percentLH64,
    displayMedium = TextSansSerif45NormalLS0LH52,
    displaySmall = TextSansSerif36NormalLS0LH44,
    headlineLarge = TextSansSerif32NormalLS0LH40,
    headlineMedium = TextSansSerif28NormalLS0LH36,
    headlineSmall = TextSansSerif24NormalLS0LH32,
    titleLarge = TextSansSerif22NormalLS0LH28,
    titleMedium = TextSansSerif16NormalLS0LH24,
    titleSmall = TextSansSerif14NormalLS10percentLH20,
    bodyLarge = TextSansSerif16NormalLS50percentLH24,
    bodyMedium = TextSansSerif14NormalLS25percentLH20,
    bodySmall = TextSansSerif12NormalLS0LH16,
    labelLarge = TextSansSerif14NormalLS10percentLH20,
    labelMedium = TextSansSerif12NormalLS50percentLH16,
    labelSmall = TextSansSerif11NormalLS50percentLH16,
)