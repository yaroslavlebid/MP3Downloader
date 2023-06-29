package com.yaroslavlebid.mp3downloader.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = Purple,
    onPrimary = RegularWhite,
    secondary = LightGray,
    onSecondary = DarkGray,
    primaryVariant = Orchid,
    background = RegularWhite,
    surface = RegularWhite,
    onBackground = RegularBlack,
    onSurface = RegularBlack,
    error = Red
)

@Composable
fun MP3DownloaderTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}