package com.example.edumension.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GamePrimaryDark,
    secondary = GameSecondaryDark,
    tertiary = GameSuccess,
    background = GameBackgroundDark,
    surface = GameSurfaceDark,
    onPrimary = GameSurfaceDark,
    onSecondary = GameTextPrimaryDark,
    onBackground = GameTextPrimaryDark,
    onSurface = GameTextPrimaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = GamePrimary,
    secondary = GameSecondary,
    tertiary = GameSuccess,
    background = GameBackground,
    surface = GameSurface,
    onPrimary = GameSurface,
    onSecondary = GameTextPrimary,
    onBackground = GameTextPrimary,
    onSurface = GameTextPrimary
)

@Composable
fun EdumensionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic color to enforce game theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}