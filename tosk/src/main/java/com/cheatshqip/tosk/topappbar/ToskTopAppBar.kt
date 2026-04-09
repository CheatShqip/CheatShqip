package com.cheatshqip.tosk.topappbar

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.cheatshqip.tosk.topappbar.tokens.ToskTopAppBarColor
import com.cheatshqip.tosk.topappbar.tokens.ToskTopAppBarTextStyle

@Composable
fun ToskTopAppBar(
    color: ToskTopAppBarColor = ToskTopAppBarColor.default(),
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
) {
    TopAppBar(
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = color.containerColor,
                titleContentColor = color.titleContentColor,
                navigationIconContentColor = color.titleContentColor,
            ),
        navigationIcon = navigationIcon,
        title = {
            CompositionLocalProvider(LocalTextStyle provides ToskTopAppBarTextStyle.default) {
                title()
            }
        },
    )
}
