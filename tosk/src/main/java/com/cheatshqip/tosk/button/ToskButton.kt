package com.cheatshqip.tosk.button

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.animateAsColor
import com.cheatshqip.tosk.tokens.ToskSpacing

sealed interface ToskButtonSize {
    val minHeight: Dp

    data object Medium : ToskButtonSize {
        override val minHeight = 46.dp
    }
}

sealed interface ToskButtonType {
    @Composable
    fun backgroundColor(interactionSource: InteractionSource): Color
    @Composable
    fun contentColor(interactionSource: InteractionSource): Color
    @Composable
    fun disabledBackgroundColor(interactionSource: InteractionSource): Color
    @Composable
    fun disabledContentColor(interactionSource: InteractionSource): Color
    val contentPadding: PaddingValues
        get() = PaddingValues(ToskSpacing.M)

    data object Primary : ToskButtonType {
        @Composable
        override fun backgroundColor(interactionSource: InteractionSource): Color {
            return interactionSource.animateAsColor(
                default = ToskTheme.colors.backgroundAccent,
                pressed = ToskTheme.colors.backgroundAccentPressed
            )
        }
        @Composable
        override fun contentColor(interactionSource: InteractionSource): Color {
            return interactionSource.animateAsColor(
                default = ToskTheme.colors.textAccent,
                pressed = ToskTheme.colors.textAccentPressed
            )
        }
        @Composable
        override fun disabledBackgroundColor(interactionSource: InteractionSource): Color {
            return ToskTheme.colors.backgroundAccentDisabled
        }
        @Composable
        override fun disabledContentColor(interactionSource: InteractionSource): Color {
            return ToskTheme.colors.textAccentDisabled
        }
    }

    data object Secondary : ToskButtonType {
        @Composable
        override fun backgroundColor(interactionSource: InteractionSource): Color {
            return interactionSource.animateAsColor(
                default = ToskTheme.colors.backgroundSecondary,
                pressed = ToskTheme.colors.backgroundSecondaryPressed
            )
        }
        @Composable
        override fun contentColor(interactionSource: InteractionSource): Color {
            return interactionSource.animateAsColor(
                default = ToskTheme.colors.textSecondary,
                pressed = ToskTheme.colors.textSecondaryPressed
            )
        }
        @Composable
        override fun disabledBackgroundColor(interactionSource: InteractionSource): Color {
            return ToskTheme.colors.backgroundSecondaryDisabled
        }
        @Composable
        override fun disabledContentColor(interactionSource: InteractionSource): Color {
            return ToskTheme.colors.textSecondaryDisabled
        }
    }
}

@Composable
fun ToskButton(
    modifier: Modifier = Modifier,
    size: ToskButtonSize = ToskButtonSize.Medium,
    type: ToskButtonType = ToskButtonType.Primary,
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
        colors = ButtonDefaults.buttonColors(
            containerColor = type.backgroundColor(interactionSource),
            contentColor = type.contentColor(interactionSource),
            disabledContainerColor = type.disabledBackgroundColor(interactionSource),
            disabledContentColor = type.disabledContentColor(interactionSource),
        ),
        contentPadding = type.contentPadding,
        content = content,
    )
}