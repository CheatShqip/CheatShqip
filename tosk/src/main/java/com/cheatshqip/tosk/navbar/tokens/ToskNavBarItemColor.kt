package com.cheatshqip.tosk.navbar.tokens

import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.tokens.primitive.ToskPalette
import com.cheatshqip.tosk.tokens.primitive.ToskRippleAlpha
import com.cheatshqip.tosk.tokens.semantic.ToskColors

data class ToskNavBarItemColor(
    val selectedIconColor: Color,
    val selectedTextColor: Color,
    val indicatorColor: Color,
    val unselectedIconColor: Color,
    val unselectedTextColor: Color,
    val disabledIconColor: Color,
    val disabledTextColor: Color,
    val ripple: RippleConfiguration,
) {
    companion object Companion {
        @Composable
        fun default(): ToskNavBarItemColor {
            val colors = ToskTheme.colors
            val ripple = RippleConfiguration(color = colors.ripple.default, rippleAlpha = ToskRippleAlpha.S)
            return when (colors) {
                ToskColors.Light -> ToskNavBarItemColor(
                    selectedIconColor = ToskPalette.crimson,
                    selectedTextColor = ToskPalette.crimson,
                    indicatorColor = colors.border.primary,
                    unselectedIconColor = colors.text.secondary,
                    unselectedTextColor = colors.text.secondary,
                    disabledIconColor = colors.text.textOnSecondaryDisabled,
                    disabledTextColor = colors.text.textOnSecondaryDisabled,
                    ripple = ripple,
                )
                ToskColors.Dark -> ToskNavBarItemColor(
                    selectedIconColor = colors.text.textOnPrimary,
                    selectedTextColor = colors.text.textOnPrimary,
                    indicatorColor = colors.border.primary,
                    unselectedIconColor = colors.text.textOnPrimaryMuted,
                    unselectedTextColor = colors.text.textOnPrimaryMuted,
                    disabledIconColor = colors.text.textOnPrimaryDisabled,
                    disabledTextColor = colors.text.textOnPrimaryDisabled,
                    ripple = ripple,
                )
            }
        }
    }
}
