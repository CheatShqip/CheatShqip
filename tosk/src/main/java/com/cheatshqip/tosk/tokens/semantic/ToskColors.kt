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
            textOnInfoRed = ToskPalette.fireBrick,
            textOnInfoBlue = ToskPalette.royalBlue,
            textOnInfoGreen = ToskPalette.forestGreen,
            textOnInfoYellow = ToskPalette.brown,
            textOnInfoOrange = ToskPalette.rust,
        ),
        background = ToskColorsBackground(
            primary = ToskPalette.crimson,
            primaryDisabled = ToskPalette.salmonPink,
            secondary = ToskPalette.alabaster,
            secondaryDisabled = ToskPalette.lightGray,
            accent = ToskPalette.lavender,
            accentDisabled = ToskPalette.lightGray,
            infoRed = ToskPalette.mistyRose,
            infoBlue = ToskPalette.aliceBlue,
            infoGreen = ToskPalette.honeydew,
            infoYellow = ToskPalette.lemonChiffon,
            infoOrange = ToskPalette.papayaWhip,
        ),
        border = ToskColorsBorder(
            primary = ToskPalette.pinkLace,
            secondary = ToskPalette.lightGray,
            accent = ToskPalette.mauve,
        )
    )

    data object Dark : ToskColors(
        text = TODO(),
        background = TODO(),
        border = TODO()
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
    val textOnInfoRed: Color,
    val textOnInfoBlue: Color,
    val textOnInfoGreen: Color,
    val textOnInfoYellow: Color,
    val textOnInfoOrange: Color,
)

data class ToskColorsBackground(
    val primary: Color,
    val primaryDisabled: Color,
    val secondary: Color,
    val secondaryDisabled: Color,
    val accent: Color,
    val accentDisabled: Color,
    val infoRed: Color,
    val infoBlue: Color,
    val infoGreen: Color,
    val infoYellow: Color,
    val infoOrange: Color,
)

data class ToskColorsBorder(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val none : Color = ToskPalette.transparent
)
