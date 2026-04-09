package com.cheatshqip.tosk.topappbar.tokens

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme

data class ToskTopAppBarColor(
    val containerColor: Color,
    val titleContentColor: Color,
) {
    companion object Companion {
        @Composable
        fun default(): ToskTopAppBarColor {
            return ToskTopAppBarColor(
                containerColor = ToskTheme.colors.background.primary,
                titleContentColor = ToskTheme.colors.text.textOnPrimary,
            )
        }
    }
}
