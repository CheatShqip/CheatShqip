package com.cheatshqip.tosk

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontFamily

private val LocalBpkTypography = staticCompositionLocalOf<BpkTypography> {
    error("Wrap you content with BpkTheme {} to get access to Backpack typography")
}
private val LocalBpkColors = staticCompositionLocalOf<BpkColors> {
    error("Wrap you content with BpkTheme {} to get access to Backpack colors")
}
private val LocalBpkShapes = staticCompositionLocalOf<BpkShapes> {
    error("Wrap you content with BpkTheme {} to get access to Backpack shapes")
}

@Composable
fun BpkTheme(
    fontFamily: FontFamily = FontFamily.SansSerif,
    content: @Composable () -> Unit,
) {
    val typography = BpkTypography(defaultFontFamily = fontFamily)
    val colors = if (isSystemInDarkTheme()) BpkColors.dark() else BpkColors.light()
    val shapes = BpkShapes()

    CompositionLocalProvider(
        LocalBpkTypography provides typography,
        LocalBpkColors provides colors,
        LocalBpkShapes provides shapes,
        LocalContentColor provides colors.textPrimary,
        LocalTextStyle provides typography.bodyDefault,
        content = content,
    )
}

object BpkTheme {

    val typography: BpkTypography
        @Composable
        @ReadOnlyComposable
        get() = if (LocalInspectionMode.current) {
            // when in preview mode return a default typography object to ensure previews work
            // without wrapping it in another composable
            BpkTypography(defaultFontFamily = FontFamily.SansSerif)
        } else {
            LocalBpkTypography.current
        }

    val colors: BpkColors
        @Composable
        @ReadOnlyComposable
        get() = if (LocalInspectionMode.current) {
            // when in preview mode return a default colour object to ensure previews work
            // without wrapping it in another composable
            if (isSystemInDarkTheme()) BpkColors.dark() else BpkColors.light()
        } else {
            LocalBpkColors.current
        }

    val shapes: BpkShapes
        @Composable
        @ReadOnlyComposable
        get() = if (LocalInspectionMode.current) {
            // when in preview mode return a default typography object to ensure previews work
            // without wrapping it in another composable
            BpkShapes()
        } else {
            LocalBpkShapes.current
        }
}