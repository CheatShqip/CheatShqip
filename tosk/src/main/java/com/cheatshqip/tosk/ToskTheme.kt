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
    error("Wrap you content with ToskTheme {} to get access to Tosk typography")
}
private val LocalToskColors = staticCompositionLocalOf<ToskColors> {
    error("Wrap you content with ToskTheme {} to get access to Tosk colors")
}

@ReadOnlyComposable
@Composable
private fun lightDarkColors(): ToskColors {
    return if (isSystemInDarkTheme()) {
        ToskColors.Dark
    } else {
        ToskColors.Light
    }
}

@Composable
fun ToskTheme(
    content: @Composable () -> Unit,
) {
    val typography = ToskTypography.Default
    val colors = lightDarkColors()

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
            ToskTypography.Default
        } else {
            LocalToskTypography.current
        }
    val colors: ToskColors
        @Composable
        @ReadOnlyComposable
        get() = if (LocalInspectionMode.current) {
            lightDarkColors()
        } else {
            LocalToskColors.current
        }
    val spacing: ToskSpacing
        @ReadOnlyComposable
        @Composable
        get() = ToskSpacing
}
