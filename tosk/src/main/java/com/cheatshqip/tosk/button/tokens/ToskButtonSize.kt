package com.cheatshqip.tosk.button.tokens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing

sealed class ToskButtonSize(
    val minHeight: Dp,
    val contentPadding: PaddingValues = PaddingValues(
        vertical = ToskSpacing.None,
        horizontal = ToskSpacing.M
    )
) {
    data object Medium : ToskButtonSize(minHeight = 40.dp)
}

