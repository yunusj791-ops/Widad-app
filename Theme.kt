package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BurgundyPrimary,
    onPrimary = Color.White,
    primaryContainer = BurgundyContainer,
    onPrimaryContainer = OnBurgundyContainer,
    secondary = ChampagneGold,
    onSecondary = CharcoalText,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = ChampagneGoldDark,
    tertiary = BurgundyLight,
    onTertiary = Color.White,
    background = WarmIvory,
    onBackground = CharcoalText,
    surface = WarmIvorySurface,
    onSurface = CharcoalText,
    surfaceVariant = WarmIvoryVariant,
    onSurfaceVariant = CharcoalMuted,
    outline = BorderSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = ChampagneGoldLight,
    onPrimary = BurgundyDark,
    primaryContainer = BurgundyLight,
    onPrimaryContainer = Color.White,
    secondary = ChampagneGold,
    onSecondary = BurgundyDark,
    secondaryContainer = BurgundyDark,
    onSecondaryContainer = ChampagneGoldLight,
    tertiary = BurgundyPrimary,
    background = Color(0xFF1A1214),
    onBackground = Color(0xFFF7F1EE),
    surface = Color(0xFF241A1D),
    onSurface = Color(0xFFF7F1EE),
    surfaceVariant = Color(0xFF332529),
    onSurfaceVariant = Color(0xFFD6C8C5),
    outline = Color(0xFF4A383D)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
