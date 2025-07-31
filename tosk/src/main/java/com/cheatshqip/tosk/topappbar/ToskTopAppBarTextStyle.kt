package com.cheatshqip.tosk.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.cheatshqip.tosk.ToskTheme

object ToskTopAppBarTextStyle {
    val default: TextStyle
        @Composable
        get() = ToskTheme.typography.heading3
}