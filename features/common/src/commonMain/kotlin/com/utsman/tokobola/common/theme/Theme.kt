package com.utsman.tokobola.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.utsman.tokobola.core.utils.parseString

private val DarkColorScheme = darkColors(
    primary = Color.parseString("#616161"),
    secondary = Color.parseString("#757575"),
    secondaryVariant = Color.White
)

private val LightColorScheme = lightColors(
    primary = Color.parseString("#607D8B"),
    secondary = Color.parseString("#9E9E9E"),
    secondaryVariant = Color.White

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CommonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorSchema = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colors = colorSchema,
        typography = Type(),
        content = content
    )
}