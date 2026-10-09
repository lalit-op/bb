package com.example.feathertv.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    onPrimary = Color.White,
    primaryContainer = NeonPurple,
    onPrimaryContainer = Color.White,
    secondary = NeonPink,
    onSecondary = Color.White,
    background = BgDark,
    onBackground = TextPrimary,
    surface = CardDark,
    onSurface = TextPrimary,
    surfaceVariant = CardDarkHover,
    onSurfaceVariant = TextSecondary,
    outline = BorderDark
)

@Composable
fun FeatherTVTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
