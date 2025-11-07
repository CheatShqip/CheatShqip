package com.cheatshqip.tosk.button.tokens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing

private const val MINIMUM_BUTTON_HEIGHT = 40

sealed class ToskButtonSize(
    val minHeight: Dp,
    val contentPadding: PaddingValues = PaddingValues(
        vertical = ToskSpacing.None,
        horizontal = ToskSpacing.M
    )
) {
    data object Medium : ToskButtonSize(minHeight = MINIMUM_BUTTON_HEIGHT.dp)
}

