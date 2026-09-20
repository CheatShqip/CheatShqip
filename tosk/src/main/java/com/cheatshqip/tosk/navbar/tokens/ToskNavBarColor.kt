package com.cheatshqip.tosk.navbar.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.tokens.primitive.ToskPalette
import com.cheatshqip.tosk.tokens.semantic.ToskColors

data class ToskNavBarColor(
    val containerColor: Color,
) {
    companion object Companion {
        @Composable
        fun default(): ToskNavBarColor {
            return ToskNavBarColor(
                containerColor = when (ToskTheme.colors) {
                    ToskColors.Light -> ToskPalette.alabaster
                    ToskColors.Dark -> ToskTheme.colors.background.primary
                },
            )
        }
    }
}
