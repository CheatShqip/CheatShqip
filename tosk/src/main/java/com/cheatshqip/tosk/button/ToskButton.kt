package com.cheatshqip.tosk.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.cheatshqip.tosk.animateAsColor
import com.cheatshqip.tosk.button.tokens.ToskButtonColor
import com.cheatshqip.tosk.button.tokens.ToskButtonSize
import com.cheatshqip.tosk.tokens.semantic.ToskShape

@Composable
fun ToskButton(
    modifier: Modifier = Modifier,
    size: ToskButtonSize = ToskButtonSize.Medium,
    color: ToskButtonColor = ToskButtonColor.primary(),
    enabled: Boolean = true,
    contentDescription: String,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .requiredHeight(size.minHeight)
            .clearAndSetSemantics {
                this.contentDescription = contentDescription
            },
        shape = ToskShape.Medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = interactionSource.animateAsColor(color.backgroundColor),
            contentColor = interactionSource.animateAsColor(color.contentColor),
            disabledContainerColor = color.disabledBackgroundColor,
            disabledContentColor = color.disabledContentColor,
        ),
        contentPadding = size.contentPadding,
        content = content,
    )
}