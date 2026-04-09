package com.cheatshqip.tosk.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.cheatshqip.tosk.card.tokens.ToskCardColor
import com.cheatshqip.tosk.card.tokens.ToskCardElevation
import com.cheatshqip.tosk.card.tokens.ToskCardSize
import com.cheatshqip.tosk.tokens.semantic.ToskShape

@Composable
fun ToskCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: ToskCardColor = ToskCardColor.default(),
    size: ToskCardSize = ToskCardSize.Default,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(size.minHeight),
        enabled = enabled,
        shape = ToskShape.Medium,
        colors = CardDefaults.cardColors(
            containerColor = color.backgroundColor,
            contentColor = color.contentColor,
        ),
        border = BorderStroke(size.borderSize, color = color.borderColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = ToskCardElevation.default,
            pressedElevation = ToskCardElevation.pressed,
            focusedElevation = ToskCardElevation.default,
            hoveredElevation = ToskCardElevation.default,
            draggedElevation = ToskCardElevation.default,
            disabledElevation = ToskCardElevation.default,
        ),
        content = {
            Column(Modifier.padding(size.contentPadding)) {
                content()
            }
        },
    )
}

@Suppress("UnusedPrivateMember")
@PreviewLightDark
@Composable
private fun ToskCardPreview() {
    ToskCard(onClick = {}) {
        Text(
            text = "This is a card",
        )
    }
}
