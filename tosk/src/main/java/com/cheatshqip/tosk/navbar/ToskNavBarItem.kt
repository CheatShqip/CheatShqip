package com.cheatshqip.tosk.navbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import com.cheatshqip.tosk.navbar.tokens.ToskNavBarItemColor
import com.cheatshqip.tosk.navbar.tokens.ToskNavBarTextStyle

@Composable
fun RowScope.ToskNavBarItem(
    modifier: Modifier = Modifier,
    color: ToskNavBarItemColor = ToskNavBarItemColor.default(),
    selected: Boolean,
    enabled: Boolean = true,
    contentDescription: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalRippleConfiguration provides color.ripple) {
        NavigationBarItem(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.clearAndSetSemantics {
                this.contentDescription = contentDescription
                this.role = Role.Tab
                this.selected = selected
                onClick {
                    onClick()
                    true
                }
            },
            icon = icon,
            label = {
                CompositionLocalProvider(LocalTextStyle provides ToskNavBarTextStyle.default) {
                    label()
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = color.selectedIconColor,
                selectedTextColor = color.selectedTextColor,
                indicatorColor = color.indicatorColor,
                unselectedIconColor = color.unselectedIconColor,
                unselectedTextColor = color.unselectedTextColor,
                disabledIconColor = color.disabledIconColor,
                disabledTextColor = color.disabledTextColor,
            ),
        )
    }
}
