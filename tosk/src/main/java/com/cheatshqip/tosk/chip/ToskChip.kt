package com.cheatshqip.tosk.chip

import android.R.attr.contentDescription
import android.R.attr.enabled
import android.R.attr.label
import android.R.attr.onClick
import android.R.attr.type
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ChipColors
import androidx.compose.material3.InputChip
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SuggestionChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.animateAsColor
import com.cheatshqip.tosk.tokens.ToskSpacing

sealed interface ToskChipSize {
    val minHeight: Dp

    data object Medium : ToskChipSize {
        override val minHeight = 56.dp
    }
}

sealed interface ToskChipType {
    @Composable
    fun backgroundColor(interactionSource: InteractionSource): Color
    @Composable
    fun contentColor(interactionSource: InteractionSource): Color
    @Composable
    fun disabledBackgroundColor(): Color
    @Composable
    fun disabledContentColor(): Color
    val contentPadding: PaddingValues
        get() = PaddingValues(vertical = ToskSpacing.None, horizontal = ToskSpacing.M)

    data object Primary : ToskChipType {
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
        override fun disabledBackgroundColor(): Color {
            return ToskTheme.colors.backgroundAccentDisabled
        }
        @Composable
        override fun disabledContentColor(): Color {
            return ToskTheme.colors.textAccentDisabled
        }
    }

    data object Secondary : ToskChipType {
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
        override fun disabledBackgroundColor(): Color {
            return ToskTheme.colors.backgroundSecondaryDisabled
        }
        @Composable
        override fun disabledContentColor(): Color {
            return ToskTheme.colors.textSecondaryDisabled
        }
    }
}

@Composable
fun ToskChip(
    modifier: Modifier = Modifier,
    size: ToskChipSize = ToskChipSize.Medium,
    type: ToskChipType = ToskChipType.Primary,
    enabled: Boolean = true,
    selected: Boolean = false,
    contentDescription: String,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {},
    label: @Composable (() -> Unit),
) {
    var active by remember { mutableStateOf(selected) }

    InputChip(
        enabled = enabled,
        modifier = modifier
            .requiredHeight(40.dp)
            .clearAndSetSemantics {
                this.contentDescription = contentDescription
            }.padding(1.dp),
        shape = RoundedCornerShape(8.dp),
        colors = SelectableChipColors(
            containerColor = ToskTheme.colors.backgroundSecondary,
            labelColor = ToskTheme.colors.textSecondary,
            leadingIconColor = ToskTheme.colors.textSecondary,
            trailingIconColor = ToskTheme.colors.textSecondary,
            selectedContainerColor = type.backgroundColor(interactionSource),
            selectedLabelColor = type.contentColor(interactionSource),
            selectedLeadingIconColor = type.contentColor(interactionSource),
            selectedTrailingIconColor = type.contentColor(interactionSource),
            disabledLabelColor = type.disabledContentColor(),
            disabledTrailingIconColor = type.disabledContentColor(),
            disabledLeadingIconColor = type.disabledContentColor(),
            disabledContainerColor = type.disabledBackgroundColor(),
            disabledSelectedContainerColor = type.disabledBackgroundColor(),
        ),
        label = label,
        elevation = null,
        border = null,
        interactionSource = interactionSource,
        selected = active,
        onClick = {
            active = !active
            onClick()
        }
    )
}