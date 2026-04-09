package com.cheatshqip.tosk.button.tokens

import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.tokens.primitive.ToskRippleAlpha

data class ToskButtonColor(
    val backgroundColor: Color,
    val contentColor: Color,
    val disabledBackgroundColor: Color,
    val disabledContentColor: Color,
    val ripple: RippleConfiguration,
) {
    companion object Companion {
        @Composable
        fun primary(): ToskButtonColor {
            return ToskButtonColor(
                backgroundColor = ToskTheme.colors.background.primary,
                contentColor = ToskTheme.colors.text.textOnPrimary,
                disabledBackgroundColor = ToskTheme.colors.background.primaryDisabled,
                disabledContentColor = ToskTheme.colors.text.textOnPrimaryDisabled,
                ripple = RippleConfiguration(
                    color = ToskTheme.colors.ripple.default,
                    rippleAlpha = ToskRippleAlpha.M
                )
            )
        }

        @Composable
        fun secondary(): ToskButtonColor {
            return ToskButtonColor(
                backgroundColor = ToskTheme.colors.background.secondary,
                contentColor = ToskTheme.colors.text.secondary,
                disabledBackgroundColor = ToskTheme.colors.background.secondaryDisabled,
                disabledContentColor = ToskTheme.colors.text.textOnSecondaryDisabled,
                ripple = RippleConfiguration(
                    color = ToskTheme.colors.ripple.default,
                    rippleAlpha = ToskRippleAlpha.S
                )
            )
        }

        @Composable
        fun accent(): ToskButtonColor {
            return ToskButtonColor(
                backgroundColor = ToskTheme.colors.background.accent,
                contentColor = ToskTheme.colors.text.accent,
                disabledBackgroundColor = ToskTheme.colors.background.accentDisabled,
                disabledContentColor = ToskTheme.colors.text.textOnAccentDisabled,
                ripple = RippleConfiguration(
                    color = ToskTheme.colors.ripple.default,
                    rippleAlpha = ToskRippleAlpha.S
                )
            )
        }
    }
}
