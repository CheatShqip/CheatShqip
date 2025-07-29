package com.cheatshqip.tosk.chip.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.StateColor
import com.cheatshqip.tosk.ToskTheme

data class ToskChipColor(
    val backgroundColor: StateColor,
    val contentColor: StateColor,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color,
) {
    companion object Companion {
        @Composable
        fun primary(): ToskChipColor {
            return ToskChipColor(
                backgroundColor = StateColor(
                    default = ToskTheme.colors.background.accent,
                    pressed = ToskTheme.colors.background.accentPressed,
                ),
                contentColor = StateColor(
                    default = ToskTheme.colors.text.accent,
                    pressed = ToskTheme.colors.text.accentPressed,
                ),
                disabledBackgroundColor = ToskTheme.colors.background.accentDisabled,
                disabledContentColor = ToskTheme.colors.text.accentDisabled,
            )
        }

        @Composable
        fun secondary(): ToskChipColor {
            return ToskChipColor(
                backgroundColor = StateColor(
                    default = ToskTheme.colors.background.secondary,
                    pressed = ToskTheme.colors.background.secondaryPressed,
                ),
                contentColor = StateColor(
                    default = ToskTheme.colors.text.secondary,
                    pressed = ToskTheme.colors.text.secondaryPressed
                ),
                disabledBackgroundColor = ToskTheme.colors.background.secondaryDisabled,
                disabledContentColor = ToskTheme.colors.text.secondaryDisabled,
            )
        }
    }
}