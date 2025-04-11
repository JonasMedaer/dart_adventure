package com.example.dartadventure.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle

private val MyLightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    primaryContainer = PrimaryTeal.copy(alpha = 0.2f),
    onPrimaryContainer = Color.Black,
    inversePrimary = PrimaryTeal,
    secondary = SecondaryYellow,
    onSecondary = Color.Black,
    secondaryContainer = SecondaryYellow.copy(alpha = 0.2f),
    onSecondaryContainer = Color.Black,
    tertiary = AccentOrange,
    onTertiary = Color.Black,
    tertiaryContainer = AccentOrange.copy(alpha = 0.2f),
    onTertiaryContainer = Color.Black,
    background = Color(0xFFF8F8F8),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFEEEEEE),
    onSurfaceVariant = Color.Black,
    surfaceTint = PrimaryTeal.copy(alpha = 0.1f),
    inverseSurface = Color(0xFF222222),
    inverseOnSurface = Color.White,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red.copy(alpha = 0.2f),
    onErrorContainer = Color.Black,
    outline = Color.Gray,
    outlineVariant = Color.LightGray,
    scrim = Color.Black.copy(alpha = 0.5f),
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
    primaryContainer = PrimaryTeal.copy(alpha = 0.8f),
    onPrimaryContainer = Color.White,
    inversePrimary = PrimaryTeal,
    secondary = SecondaryYellow,
    onSecondary = Color.Black,
    secondaryContainer = SecondaryYellow.copy(alpha = 0.8f),
    onSecondaryContainer = Color.Black,
    tertiary = AccentOrange,
    onTertiary = Color.Black,
    tertiaryContainer = AccentOrange.copy(alpha = 0.8f),
    onTertiaryContainer = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = Color.White,
    surfaceTint = PrimaryTeal.copy(alpha = 0.2f),
    inverseSurface = Color(0xFFEEEEEE),
    inverseOnSurface = Color.Black,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red.copy(alpha = 0.8f),
    onErrorContainer = Color.White,
    outline = Color.Gray,
    outlineVariant = Color.DarkGray,
    scrim = Color.Black.copy(alpha = 0.8f),
    surfaceBright = Color(0xFF333333),
    surfaceDim = Color(0xFF121212),
    surfaceContainer = Color(0xFF222222),
    surfaceContainerHigh = Color(0xFF2A2A2A),
    surfaceContainerHighest = Color(0xFF333333),
    surfaceContainerLow = Color(0xFF1A1A1A),
    surfaceContainerLowest = Color(0xFF121212),
)

@Composable
fun ScaledTypography(): Typography {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600 // Adjust threshold as needed
    val scaleFactor = if (isTablet) 2f else 1.1f

    return Typography(
        displayLarge = MaterialTheme.typography.displayLarge.scaled(scaleFactor),
        displayMedium = MaterialTheme.typography.displayMedium.scaled(scaleFactor),
        displaySmall = MaterialTheme.typography.displaySmall.scaled(scaleFactor),
        headlineLarge = MaterialTheme.typography.headlineLarge.scaled(scaleFactor),
        headlineMedium = MaterialTheme.typography.headlineMedium.scaled(scaleFactor),
        headlineSmall = MaterialTheme.typography.headlineSmall.scaled(scaleFactor),
        titleLarge = MaterialTheme.typography.titleLarge.scaled(scaleFactor),
        titleMedium = MaterialTheme.typography.titleMedium.scaled(scaleFactor),
        titleSmall = MaterialTheme.typography.titleSmall.scaled(scaleFactor),
        bodyLarge = MaterialTheme.typography.bodyLarge.scaled(scaleFactor),
        bodyMedium = MaterialTheme.typography.bodyMedium.scaled(scaleFactor),
        bodySmall = MaterialTheme.typography.bodySmall.scaled(scaleFactor),
        labelLarge = MaterialTheme.typography.labelLarge.scaled(scaleFactor),
        labelMedium = MaterialTheme.typography.labelMedium.scaled(scaleFactor),
        labelSmall = MaterialTheme.typography.labelSmall.scaled(scaleFactor)
    )
}

private fun TextStyle.scaled(scaleFactor: Float): TextStyle {
    return this.copy(fontSize = this.fontSize * scaleFactor)
}

@Composable
fun DartAdventureTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MyDarkColorScheme else MyLightColorScheme
    val typography = ScaledTypography()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}