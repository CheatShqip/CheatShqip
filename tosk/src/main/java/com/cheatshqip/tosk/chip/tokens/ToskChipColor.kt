package com.cheatshqip.tosk.chip.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.StateColor
import com.cheatshqip.tosk.ToskTheme

data class ToskChipColor(
    val backgroundColor: Color,
    val contentColor: Color,
    val borderColor: Color,
    val selectedBackgroundColor: StateColor,
    val selectedContentColor: StateColor,
    val selectedBorderColor: Color,
) {
    companion object Companion {
        @Composable
        fun default(): ToskChipColor {
            return ToskChipColor(
                backgroundColor = ToskTheme.colors.background.secondary,
                contentColor = ToskTheme.colors.text.secondary,
                borderColor = ToskTheme.colors.border.none,
                selectedBackgroundColor = StateColor(
                    default = ToskTheme.colors.background.accent,
                    pressed = ToskTheme.colors.background.accentPressed,
                ),
                selectedContentColor = StateColor(
                    default = ToskTheme.colors.text.accent,
                    pressed = ToskTheme.colors.text.accentPressed,
                ),
                selectedBorderColor = ToskTheme.colors.border.accent,
            )
        }
    }
}