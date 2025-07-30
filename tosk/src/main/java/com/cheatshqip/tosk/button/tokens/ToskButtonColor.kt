package com.cheatshqip.tosk.button.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.StateColor
import com.cheatshqip.tosk.ToskTheme

data class ToskButtonColor(
    val backgroundColor: StateColor,
    val contentColor: StateColor,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color,
) {
    companion object Companion {
        @Composable
        fun primary(): ToskButtonColor {
            return ToskButtonColor(
                backgroundColor = StateColor(
                    default = ToskTheme.colors.background.primary,
                    pressed = ToskTheme.colors.background.primaryPressed,
                ),
                contentColor = StateColor(
                    default = ToskTheme.colors.text.textOnPrimary,
                    pressed = ToskTheme.colors.text.textOnPrimaryPressed,
                ),
                disabledBackgroundColor = ToskTheme.colors.background.primaryDisabled,
                disabledContentColor = ToskTheme.colors.text.textOnPrimaryDisabled,
            )
        }

        @Composable
        fun secondary(): ToskButtonColor {
            return ToskButtonColor(
                backgroundColor = StateColor(
                    default = ToskTheme.colors.background.secondary,
                    pressed = ToskTheme.colors.background.secondaryPressed,
                ),
                contentColor = StateColor(
                    default = ToskTheme.colors.text.secondary,
                    pressed = ToskTheme.colors.text.textOnSecondaryPressed
                ),
                disabledBackgroundColor = ToskTheme.colors.background.secondaryDisabled,
                disabledContentColor = ToskTheme.colors.text.textOnSecondaryDisabled,
            )
        }

        @Composable
        fun accent(): ToskButtonColor {
            return ToskButtonColor(
                backgroundColor = StateColor(
                    default = ToskTheme.colors.background.accent,
                    pressed = ToskTheme.colors.background.accentPressed,
                ),
                contentColor = StateColor(
                    default = ToskTheme.colors.text.accent,
                    pressed = ToskTheme.colors.text.textOnAccentPressed,
                ),
                disabledBackgroundColor = ToskTheme.colors.background.accentDisabled,
                disabledContentColor = ToskTheme.colors.text.textOnAccentDisabled,
            )
        }
    }
}