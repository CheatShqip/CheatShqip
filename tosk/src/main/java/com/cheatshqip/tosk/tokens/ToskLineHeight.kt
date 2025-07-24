package com.cheatshqip.tosk.tokens

import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

internal object ToskLineHeight {
    internal val XS: TextUnit = 16.0.sp

    internal val S: TextUnit = 20.0.sp

    internal val M: TextUnit = 28.0.sp

    internal val L: TextUnit = 32.0.sp

    internal val XL: TextUnit = 40.0.sp

    internal val XXL: TextUnit = 48.0.sp

    internal val `3XL`: TextUnit = 56.0.sp

    internal val `4XL`: TextUnit = 72.0.sp

    internal val `5XL`: TextUnit = 84.0.sp

    internal val `6XL`: TextUnit = 96.0.sp

    internal val `7XL`: TextUnit = 120.0.sp
}

internal fun defaultLineHeightStyle(): LineHeightStyle = LineHeightStyle(
    alignment = defaultLightHeightStyleAlignment(),
    trim = LineHeightStyle.Trim.None,
)

private fun defaultLightHeightStyleAlignment(): LineHeightStyle.Alignment = LineHeightStyle.Alignment(topRatio = 0.2f)
