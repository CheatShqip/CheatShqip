package com.cheatshqip.tosk.card.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme

data class ToskCardColor(
    val backgroundColor: Color,
    val contentColor: Color,
    val borderColor: Color,
) {
    companion object Companion {
        @Composable
        fun default(): ToskCardColor {
            return ToskCardColor(
                backgroundColor = ToskTheme.colors.background.secondary,
                contentColor = ToskTheme.colors.text.primary,
                borderColor = ToskTheme.colors.border.secondary,
            )
        }
    }
}
