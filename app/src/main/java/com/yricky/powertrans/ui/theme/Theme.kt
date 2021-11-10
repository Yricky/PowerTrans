package com.yricky.powertrans.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = OhPrimary,
    primaryVariant = OhPrimary,
    secondary = Teal200,
    background = Color.Transparent
)

private val DarkColorPalette = darkColors(
    primary = OhPrimaryDark,
    primaryVariant = OhPrimaryDark,
    secondary = Teal200,
    background = Color.Transparent
)

@Composable
fun PowerTransTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}