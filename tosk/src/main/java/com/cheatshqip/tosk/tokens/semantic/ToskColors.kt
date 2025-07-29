package com.cheatshqip.tosk.tokens.semantic

import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.tokens.primitive.ToskPalette

sealed class ToskColors(
    val text: ToskColorsText,
    val background: ToskColorsBackground,
    val border: ToskColorsBorder,
) {
    data object Light : ToskColors(
        text = ToskColorsText(
            primary = ToskPalette.black,
            secondary = ToskPalette.slateGray,
            secondaryPressed = ToskPalette.charcoal,
            secondaryDisabled = ToskPalette.silver,
            accent = ToskPalette.purple,
            accentPressed = ToskPalette.deepPurple,
            accentDisabled = ToskPalette.silver,
            holorRed = ToskPalette.fireBrick,
            holoBlue = ToskPalette.royalBlue,
            holoGreen = ToskPalette.forestGreen,
            holoBrown = ToskPalette.brown,
            holoOrange = ToskPalette.rust,
        ),
        background = ToskColorsBackground(
            primary = ToskPalette.crimson,
            secondary = ToskPalette.alabaster,
            secondaryPressed = ToskPalette.lightGray,
            secondaryDisabled = ToskPalette.lightGray,
            accent = ToskPalette.lavender,
            accentPressed = ToskPalette.mauve,
            accentDisabled = ToskPalette.lightGray,
            holoRed = ToskPalette.mistyRose,
            holoBlue = ToskPalette.aliceBlue,
            holoGreen = ToskPalette.honeydew,
            holoYellow = ToskPalette.lemonChiffon,
            holoOrange = ToskPalette.papayaWhip,
        ),
        border = ToskColorsBorder(
            primary = ToskPalette.pinkLace,
            secondary = ToskPalette.lightGray,
            accent = ToskPalette.mauve,
        )
    )
}

data class ToskColorsText(
    val primary: Color,
    val secondary: Color,
    val secondaryPressed: Color,
    val secondaryDisabled: Color,
    val accent: Color,
    val accentPressed: Color,
    val accentDisabled: Color,
    val holorRed: Color,
    val holoBlue: Color,
    val holoGreen: Color,
    val holoBrown: Color,
    val holoOrange: Color,
)

data class ToskColorsBackground(
    val primary: Color,
    val secondary: Color,
    val secondaryPressed: Color,
    val secondaryDisabled: Color,
    val accent: Color,
    val accentPressed: Color,
    val accentDisabled: Color,
    val holoRed: Color,
    val holoBlue: Color,
    val holoGreen: Color,
    val holoYellow: Color,
    val holoOrange: Color,
)

data class ToskColorsBorder(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
)
