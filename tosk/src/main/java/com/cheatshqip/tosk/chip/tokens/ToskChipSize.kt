package com.cheatshqip.tosk.chip.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.tokens.primitive.ToskBorderSize

sealed class ToskChipSize(
    val minHeight: Dp,
    val borderSize: Dp,
) {
    data object Medium : ToskChipSize(
        minHeight = 40.dp,
        borderSize = ToskBorderSize.M,
    )
}

