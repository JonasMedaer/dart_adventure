package com.example.dartadventure.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MyLightColorScheme = ColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    primaryContainer = PrimaryTeal.copy(alpha = 0.2f), // Example: Lighter shade
    onPrimaryContainer = Color.Black,
    inversePrimary = PrimaryTeal,
    secondary = SecondaryYellow,
    onSecondary = Color.Black,
    secondaryContainer = SecondaryYellow.copy(alpha = 0.2f), // Example: Lighter shade
    onSecondaryContainer = Color.Black,
    tertiary = AccentOrange,
    onTertiary = Color.Black,
    tertiaryContainer = AccentOrange.copy(alpha = 0.2f), // Example: Lighter shade
    onTertiaryContainer = Color.Black,
    background = Color(0xFFF8F8F8),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFEEEEEE), // Example: Slightly different surface
    onSurfaceVariant = Color.Black,
    surfaceTint = PrimaryTeal.copy(alpha = 0.1f), // Example: Subtle tint
    inverseSurface = Color(0xFF222222), // Example: Darker surface
    inverseOnSurface = Color.White,
    error = Color.Red, // Example: Error color
    onError = Color.White,
    errorContainer = Color.Red.copy(alpha = 0.2f), // Example: Lighter error
    onErrorContainer = Color.Black,
    outline = Color.Gray, // Example: Outline color
    outlineVariant = Color.LightGray, // Example: Lighter outline
    scrim = Color.Black.copy(alpha = 0.5f), // Example: Scrim color
    surfaceBright = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFDDDDDD),
    surfaceContainer = Color(0xFFF0F0F0),
    surfaceContainerHigh = Color(0xFFE8E8E8),
    surfaceContainerHighest = Color(0xFFE0E0E0),
    surfaceContainerLow = Color(0xFFF4F4F4),
    surfaceContainerLowest = Color(0xFFFAFAFA),
)

private val MyDarkColorScheme = ColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    primaryContainer = PrimaryTeal.copy(alpha = 0.8f), // Example: Darker shade
    onPrimaryContainer = Color.White,
    inversePrimary = PrimaryTeal,
    secondary = SecondaryYellow,
    onSecondary = Color.Black,
    secondaryContainer = SecondaryYellow.copy(alpha = 0.8f), // Example: Darker shade
    onSecondaryContainer = Color.Black,
    tertiary = AccentOrange,
    onTertiary = Color.Black,
    tertiaryContainer = AccentOrange.copy(alpha = 0.8f), // Example: Darker shade
    onTertiaryContainer = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF333333), // Example: Slightly different surface
    onSurfaceVariant = Color.White,
    surfaceTint = PrimaryTeal.copy(alpha = 0.2f), // Example: Subtle tint
    inverseSurface = Color(0xFFEEEEEE), // Example: Lighter surface
    inverseOnSurface = Color.Black,
    error = Color.Red, // Example: Error color
    onError = Color.White,
    errorContainer = Color.Red.copy(alpha = 0.8f), // Example: Darker error
    onErrorContainer = Color.White,
    outline = Color.Gray, // Example: Outline color
    outlineVariant = Color.DarkGray, // Example: Darker outline
    scrim = Color.Black.copy(alpha = 0.8f), // Example: Scrim color
    surfaceBright = Color(0xFF333333),
    surfaceDim = Color(0xFF121212),
    surfaceContainer = Color(0xFF222222),
    surfaceContainerHigh = Color(0xFF2A2A2A),
    surfaceContainerHighest = Color(0xFF333333),
    surfaceContainerLow = Color(0xFF1A1A1A),
    surfaceContainerLowest = Color(0xFF121212),
)

@Composable
fun DartAdventureTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MyDarkColorScheme else MyLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}