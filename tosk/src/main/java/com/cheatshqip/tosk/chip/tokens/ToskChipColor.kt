package com.cheatshqip.tosk.chip.tokens

import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.tokens.primitive.ToskRippleAlpha

data class ToskChipColor(
    val backgroundColor: Color,
    val contentColor: Color,
    val borderColor: Color,
    val selectedBackgroundColor: Color,
    val selectedContentColor: Color,
    val selectedBorderColor: Color,
    val ripple: RippleConfiguration,
) {
    companion object Companion {
        @Composable
        fun default(): ToskChipColor {
            return ToskChipColor(
                backgroundColor = ToskTheme.colors.background.secondary,
                contentColor = ToskTheme.colors.text.secondary,
                borderColor = ToskTheme.colors.border.none,
                selectedBackgroundColor = ToskTheme.colors.background.accent,
                selectedContentColor = ToskTheme.colors.text.accent,
                selectedBorderColor = ToskTheme.colors.border.accent,
                ripple = RippleConfiguration(
                    color = ToskTheme.colors.ripple.default,
                    rippleAlpha = ToskRippleAlpha.S
                )
            )
        }
    }
}