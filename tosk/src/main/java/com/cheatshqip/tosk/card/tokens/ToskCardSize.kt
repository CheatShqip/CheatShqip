package com.cheatshqip.tosk.card.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.tokens.primitive.ToskBorderSize
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing

sealed class ToskCardSize(
    val minHeight: Dp = 100.dp,
    val contentPadding: Dp = ToskSpacing.M,
    val borderSize: Dp = ToskBorderSize.M,
) {
    data object Default : ToskCardSize()
}
