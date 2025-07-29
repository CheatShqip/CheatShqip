package com.cheatshqip.tosk.chip

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.animateAsColor
import com.cheatshqip.tosk.chip.tokens.ToskChipColor
import com.cheatshqip.tosk.chip.tokens.ToskChipSize

@Composable
fun ToskChip(
    modifier: Modifier = Modifier,
    size: ToskChipSize = ToskChipSize.Medium,
    color: ToskChipColor = ToskChipColor.primary(),
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
            .requiredHeight(size.minHeight)
            .clearAndSetSemantics {
                this.contentDescription = contentDescription
            },
        shape = RoundedCornerShape(8.dp),
        colors = SelectableChipColors(
            containerColor = ToskTheme.colors.background.secondary,
            labelColor = ToskTheme.colors.text.secondary,
            leadingIconColor = ToskTheme.colors.text.secondary,
            trailingIconColor = ToskTheme.colors.text.secondary,
            selectedContainerColor = interactionSource.animateAsColor(color.backgroundColor),
            selectedLabelColor = interactionSource.animateAsColor(color.contentColor),
            selectedLeadingIconColor = interactionSource.animateAsColor(color.contentColor),
            selectedTrailingIconColor = interactionSource.animateAsColor(color.contentColor),
            disabledLabelColor = color.disabledContentColor,
            disabledTrailingIconColor = color.disabledContentColor,
            disabledLeadingIconColor = color.disabledContentColor,
            disabledContainerColor = color.disabledBackgroundColor,
            disabledSelectedContainerColor = color.disabledBackgroundColor,
        ),
        label = label,
        elevation = null,
        border = InputChipDefaults.inputChipBorder(
            enabled = enabled,
            selected = active,
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            selectedBorderColor = ToskTheme.colors.border.accent,
            selectedBorderWidth = 2.dp
        ),
        interactionSource = interactionSource,
        selected = active,
        onClick = {
            active = !active
            onClick()
        }
    )
}