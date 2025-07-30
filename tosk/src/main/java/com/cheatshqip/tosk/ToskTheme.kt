package com.cheatshqip.tosk

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing
import com.cheatshqip.tosk.tokens.semantic.ToskColors
import com.cheatshqip.tosk.tokens.semantic.ToskTypography

private val LocalToskTypography = staticCompositionLocalOf<ToskTypography> {
    error("Wrap you content with ToskTheme {} to get access to Backpack typography")
}
private val LocalToskColors = staticCompositionLocalOf<ToskColors> {
    error("Wrap you content with ToskTheme {} to get access to Backpack colors")
}

@Composable
fun ToskTheme(
    content: @Composable () -> Unit,
) {
    val typography = ToskTypography.Default
    val colors = if (isSystemInDarkTheme()) ToskColors.Light else ToskColors.Light // TODO: change once dark theme is implemented

    CompositionLocalProvider(
        LocalToskTypography provides typography,
        LocalToskColors provides colors,
        content = content,
    )
}

object ToskTheme {

    val typography: ToskTypography
        @Composable
        @ReadOnlyComposable
        get() = if (LocalInspectionMode.current) {
            // when in preview mode return a default typography object to ensure previews work
            // without wrapping it in another composable
            ToskTypography.Default
        } else {
            LocalToskTypography.current
        }

    val colors: ToskColors
        @Composable
        @ReadOnlyComposable
        get() = if (LocalInspectionMode.current) {
            // when in preview mode return a default colour object to ensure previews work
            // without wrapping it in another composable
            if (isSystemInDarkTheme()) ToskColors.Light else ToskColors.Light // TODO: change once dark theme is implemented
        } else {
            LocalToskColors.current
        }

    val spacing: ToskSpacing
        @Composable
        get() = ToskSpacing
}