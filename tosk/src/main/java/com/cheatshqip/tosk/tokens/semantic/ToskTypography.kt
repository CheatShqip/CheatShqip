package com.cheatshqip.tosk.tokens.semantic

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.cheatshqip.tosk.tokens.primitive.ToskFontSize
import com.cheatshqip.tosk.tokens.primitive.ToskLetterSpacing
import com.cheatshqip.tosk.tokens.primitive.ToskLineHeight
import com.cheatshqip.tosk.tokens.primitive.defaultLineHeightStyle

val defaultFontFamily = FontFamily.SansSerif

sealed class ToskTypography(
    val body: TextStyle,
    val caption: TextStyle,
    val footnote: TextStyle,
    val heading1: TextStyle,
    val heading2: TextStyle,
    val heading3: TextStyle,
    val heading4: TextStyle,
    val heading5: TextStyle,
    val hero1: TextStyle,
    val hero2: TextStyle,
    val hero3: TextStyle,
    val hero4: TextStyle,
    val hero5: TextStyle,
    val label1: TextStyle,
    val label2: TextStyle,
    val label3: TextStyle,
    val subheading: TextStyle,
) {
    data object Default : ToskTypography(
        body = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = ToskFontSize.M,
            lineHeight = ToskLineHeight.M,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        caption = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = ToskFontSize.XS,
            lineHeight = ToskLineHeight.XS,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        footnote = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = ToskFontSize.S,
            lineHeight = ToskLineHeight.S,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        heading1 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.`3XL`,
            lineHeight = ToskLineHeight.`3XL`,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        heading2 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.XXL,
            lineHeight = ToskLineHeight.XXL,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        heading3 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.XL,
            lineHeight = ToskLineHeight.XL,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        heading4 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.L,
            lineHeight = ToskLineHeight.L,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        heading5 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.M,
            lineHeight = ToskLineHeight.M,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        hero1 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.`8xl`,
            lineHeight = ToskLineHeight.`7XL`,
            letterSpacing = ToskLetterSpacing.XS,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        hero2 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.`7xl`,
            lineHeight = ToskLineHeight.`6XL`,
            letterSpacing = ToskLetterSpacing.XS,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        hero3 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.`6xl`,
            lineHeight = ToskLineHeight.`5XL`,
            letterSpacing = ToskLetterSpacing.XS,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        hero4 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.`4XL`,
            lineHeight = ToskLineHeight.`4XL`,
            letterSpacing = ToskLetterSpacing.XS,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        hero5 = TextStyle(
            fontWeight = FontWeight.Black,
            fontSize = ToskFontSize.`5XL`,
            lineHeight = ToskLineHeight.`3XL`,
            letterSpacing = ToskLetterSpacing.XS,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        label1 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.M,
            lineHeight = ToskLineHeight.M,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        label2 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.S,
            lineHeight = ToskLineHeight.S,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        label3 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = ToskFontSize.XS,
            lineHeight = ToskLineHeight.XS,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
        subheading = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = ToskFontSize.XL,
            lineHeight = ToskLineHeight.L,
            fontFamily = defaultFontFamily,
            lineHeightStyle = defaultLineHeightStyle(),
        ),
    )
}
