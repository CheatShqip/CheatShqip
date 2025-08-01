package com.cheatshqip.tosk.badge

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.cheatshqip.tosk.badge.tokens.ToskBadgeColor
import com.cheatshqip.tosk.badge.tokens.ToskBadgeSize
import com.cheatshqip.tosk.badge.tokens.ToskBadgeTextStyle

@Composable
fun ToskBadge(
    modifier: Modifier = Modifier,
    color: ToskBadgeColor,
    content: @Composable RowScope.() -> Unit
) {
    Badge(
        modifier = modifier,
        containerColor = color.backgroundColor,
        contentColor = color.contentColor,
        content = {
            ContentContainer(content)
        }
    )
}

@Composable
private fun ContentContainer(content: @Composable (RowScope.() -> Unit)) {
    Row(
        modifier = Modifier.padding(
            horizontal = ToskBadgeSize.horizontalPadding,
            vertical = ToskBadgeSize.verticalPadding,
        )
    ) {
        CompositionLocalProvider(LocalTextStyle provides ToskBadgeTextStyle.content) {
            content()
        }
    }
}