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
            textOnInfoRed = ToskPalette.fireBrick,
            textOnInfoBlue = ToskPalette.royalBlue,
            textOnInfoGreen = ToskPalette.forestGreen,
            textOnInfoYellow = ToskPalette.brown,
            textOnInfoOrange = ToskPalette.rust,
        ),
        background = ToskColorsBackground(
            primary = ToskPalette.crimson,
            secondary = ToskPalette.alabaster,
            secondaryPressed = ToskPalette.lightGray,
            secondaryDisabled = ToskPalette.lightGray,
            accent = ToskPalette.lavender,
            accentPressed = ToskPalette.mauve,
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

data class ToskColorsText(
    val primary: Color,
    val secondary: Color,
    val secondaryPressed: Color,
    val secondaryDisabled: Color,
    val accent: Color,
    val accentPressed: Color,
    val accentDisabled: Color,
    val textOnInfoRed: Color,
    val textOnInfoBlue: Color,
    val textOnInfoGreen: Color,
    val textOnInfoYellow: Color,
    val textOnInfoOrange: Color,
)

data class ToskColorsBackground(
    val primary: Color,
    val secondary: Color,
    val secondaryPressed: Color,
    val secondaryDisabled: Color,
    val accent: Color,
    val accentPressed: Color,
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
