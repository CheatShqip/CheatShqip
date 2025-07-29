package com.cheatshqip.tosk.chip.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class ToskChipSize(
    val minHeight: Dp,
) {
    data object Medium : ToskChipSize(minHeight = 40.dp) // TODO : extraire en size
}

