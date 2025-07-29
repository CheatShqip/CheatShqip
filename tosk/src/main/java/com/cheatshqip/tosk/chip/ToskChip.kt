package com.cheatshqip.tosk.chip

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.InputChip
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.animateAsColor
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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
                default = ToskTheme.colors.background.accent,
                pressed = ToskTheme.colors.background.accentPressed
            )
        }
        @Composable
        override fun contentColor(interactionSource: InteractionSource): Color {
            return interactionSource.animateAsColor(
                default = ToskTheme.colors.text.accent,
                pressed = ToskTheme.colors.text.accentPressed
            )
        }
        @Composable
        override fun disabledBackgroundColor(): Color {
            return ToskTheme.colors.background.accentDisabled
        }
        @Composable
        override fun disabledContentColor(): Color {
            return ToskTheme.colors.text.accentDisabled
        }
    }

    data object Secondary : ToskChipType {
        @Composable
        override fun backgroundColor(interactionSource: InteractionSource): Color {
            return interactionSource.animateAsColor(
                default = ToskTheme.colors.background.secondary,
                pressed = ToskTheme.colors.background.secondaryPressed
            )
        }
        @Composable
        override fun contentColor(interactionSource: InteractionSource): Color {
            return interactionSource.animateAsColor(
                default = ToskTheme.colors.text.secondary,
                pressed = ToskTheme.colors.text.secondaryPressed
            )
        }
        @Composable
        override fun disabledBackgroundColor(): Color {
            return ToskTheme.colors.background.secondaryDisabled
        }
        @Composable
        override fun disabledContentColor(): Color {
            return ToskTheme.colors.text.secondaryDisabled
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
            }//.padding(1.dp)
            //.border(2.dp, ToskTheme.colors.borderAccent, shape = RoundedCornerShape(10.dp))
            .applyIf(active) {
                border(2.dp, ToskTheme.colors.border.accent, shape = RoundedCornerShape(8.dp))
            }.clipToBounds(),
        shape = RoundedCornerShape(8.dp),
        colors = SelectableChipColors(
            containerColor = ToskTheme.colors.background.secondary,
            labelColor = ToskTheme.colors.text.secondary,
            leadingIconColor = ToskTheme.colors.text.secondary,
            trailingIconColor = ToskTheme.colors.text.secondary,
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

@OptIn(ExperimentalContracts::class)
internal inline fun Modifier.applyIf(predicate: Boolean, block: Modifier.() -> Modifier): Modifier {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (predicate) block() else this
}