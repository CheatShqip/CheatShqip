package com.cheatshqip.tosk.badge.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme

data class ToskBadgeColor(
    val backgroundColor: Color,
    val contentColor: Color,
) {
    companion object {
        @Composable
        fun info1(): ToskBadgeColor = ToskBadgeColor(
            backgroundColor = ToskTheme.colors.background.info1,
            contentColor = ToskTheme.colors.text.textOnInfo1,
        )

        @Composable
        fun info2(): ToskBadgeColor = ToskBadgeColor(
            backgroundColor = ToskTheme.colors.background.info2,
            contentColor = ToskTheme.colors.text.textOnInfo2,
        )

        @Composable
        fun info3(): ToskBadgeColor = ToskBadgeColor(
            backgroundColor = ToskTheme.colors.background.info3,
            contentColor = ToskTheme.colors.text.textOnInfo3,
        )

        @Composable
        fun info4(): ToskBadgeColor = ToskBadgeColor(
            backgroundColor = ToskTheme.colors.background.info4,
            contentColor = ToskTheme.colors.text.textOnInfo4,
        )

        @Composable
        fun info5(): ToskBadgeColor = ToskBadgeColor(
            backgroundColor = ToskTheme.colors.background.info5,
            contentColor = ToskTheme.colors.text.textOnInfo5,
        )
    }
}

