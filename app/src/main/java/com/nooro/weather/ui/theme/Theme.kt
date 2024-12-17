package com.nooro.weather.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    //TODO Implement dark color scheme
)

private val LightColorScheme = lightColorScheme(
    // The primary color is the color displayed most frequently across your app’s screens and components.
    primary = Black,
    // The preferred tonal color of containers.
    primaryContainer = LightGrey,
    // The color (and state variants) that should be used for content on top of primaryContainer.
    onPrimaryContainer = DarkGrey,
    // The background color that appears behind scrollable content.
    background = Color.White,
    // Color used for text and icons displayed on top of the background color.
    onBackground = Black,
    // The surface color that affect surfaces of components, such as cards, sheets, and menus.
    surface = White,
    // The color (and state variants) that can be used for content on top of surface.
    onSurfaceVariant = DarkGrey,
)

@Composable
fun NooroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, //TODO Do we want to support this?
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            //TODO Re-enabled dark/light theme once implemented
            //if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = Typography,
        content = content
    )
}