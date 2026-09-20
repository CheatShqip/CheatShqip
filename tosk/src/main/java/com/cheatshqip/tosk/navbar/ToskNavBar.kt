package com.cheatshqip.tosk.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.navbar.tokens.ToskNavBarColor

@Composable
fun ToskNavBar(
    modifier: Modifier = Modifier,
    color: ToskNavBarColor = ToskNavBarColor.default(),
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = color.containerColor,
        windowInsets = windowInsets,
        content = content,
    )
}

@Composable
private fun PreviewIcon() {
    Box(modifier = Modifier.size(24.dp).background(Color.Gray))
}

@Suppress("UnusedPrivateMember")
@PreviewLightDark
@Composable
private fun ToskNavBarPreview() {
    ToskNavBar {
        ToskNavBarItem(
            selected = true,
            contentDescription = "Translate tab",
            onClick = {},
            icon = { PreviewIcon() },
            label = { Text("Translate") },
        )
        ToskNavBarItem(
            selected = false,
            contentDescription = "Decline tab",
            onClick = {},
            icon = { PreviewIcon() },
            label = { Text("Decline") },
        )
        ToskNavBarItem(
            selected = false,
            contentDescription = "Conjugate tab",
            onClick = {},
            icon = { PreviewIcon() },
            label = { Text("Conjugate") },
        )
    }
}
