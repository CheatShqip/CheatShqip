package com.cheatshqip.tosk.tokens.semantic

import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.tokens.primitive.ToskPalette

sealed class ToskColors(
    val text: ToskColorsText,
    val background: ToskColorsBackground,
    val border: ToskColorsBorder,
    val ripple: ToskRipple = ToskRipple
) {
    data object Light : ToskColors(
        text = ToskColorsText(
            primary = ToskPalette.black,
            textOnPrimary = ToskPalette.alabaster,
            textOnPrimaryDisabled = ToskPalette.coralReef,
            secondary = ToskPalette.slateGray,
            textOnSecondaryDisabled = ToskPalette.silver,
            accent = ToskPalette.purple,
            textOnAccentDisabled = ToskPalette.silver,
            textOnInfo1 = ToskPalette.fireBrick,
            textOnInfo2 = ToskPalette.royalBlue,
            textOnInfo3 = ToskPalette.forestGreen,
            textOnInfo4 = ToskPalette.brown,
            textOnInfo5 = ToskPalette.rust,
            error = ToskPalette.crimson,
        ),
        background = ToskColorsBackground(
            primary = ToskPalette.crimson,
            primaryDisabled = ToskPalette.salmonPink,
            secondary = ToskPalette.alabaster,
            secondaryDisabled = ToskPalette.lightGray,
            accent = ToskPalette.lavender,
            accentDisabled = ToskPalette.lightGray,
            info1 = ToskPalette.mistyRose,
            info2 = ToskPalette.aliceBlue,
            info3 = ToskPalette.honeydew,
            info4 = ToskPalette.lemonChiffon,
            info5 = ToskPalette.papayaWhip,
        ),
        border = ToskColorsBorder(
            primary = ToskPalette.pinkLace,
            secondary = ToskPalette.lightGray,
            accent = ToskPalette.mauve,
        )
    )

    data object Dark : ToskColors(
        text = ToskColorsText(
            primary = ToskPalette.alabaster,
            textOnPrimary = ToskPalette.alabaster,
            textOnPrimaryDisabled = ToskPalette.alabaster,
            secondary = ToskPalette.alabaster,
            textOnSecondaryDisabled = ToskPalette.alabaster,
            accent = ToskPalette.purple,
            textOnAccentDisabled = ToskPalette.alabaster,
            textOnInfo1 = ToskPalette.salmonPink,
            textOnInfo2 = ToskPalette.aliceBlue,
            textOnInfo3 = ToskPalette.honeydew,
            textOnInfo4 = ToskPalette.lemonChiffon,
            textOnInfo5 = ToskPalette.papayaWhip,
            error = ToskPalette.crimson,
        ),
        background = ToskColorsBackground(
            primary = ToskPalette.crimson,
            primaryDisabled = ToskPalette.fireBrick,
            secondary = ToskPalette.charcoal,
            secondaryDisabled = ToskPalette.jet,
            accent = ToskPalette.darkPurple,
            accentDisabled = ToskPalette.jet,
            info1 = ToskPalette.maroon,
            info2 = ToskPalette.navy,
            info3 = ToskPalette.darkGreen,
            info4 = ToskPalette.darkGoldenrod,
            info5 = ToskPalette.saddleBrown,
        ),
        border = ToskColorsBorder(
            primary = ToskPalette.fireBrick,
            secondary = ToskPalette.jet,
            accent = ToskPalette.darkPurple,
        )
    )
}

object ToskRipple {
    val default = ToskPalette.black
}

data class ToskColorsText(
    val primary: Color,
    val textOnPrimary: Color,
    val textOnPrimaryDisabled: Color,
    val secondary: Color,
    val textOnSecondaryDisabled: Color,
    val accent: Color,
    val textOnAccentDisabled: Color,
    val textOnInfo1: Color,
    val textOnInfo2: Color,
    val textOnInfo3: Color,
    val textOnInfo4: Color,
    val textOnInfo5: Color,
    val error: Color,
)

data class ToskColorsBackground(
    val primary: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val secondaryDisabled: Color,
    val accent: Color,
    val accentDisabled: Color,
    val info1: Color,
    val info2: Color,
    val info3: Color,
    val info4: Color,
    val info5: Color,
)

data class ToskColorsBorder(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val none: Color = ToskPalette.transparent
)
