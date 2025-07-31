package com.cheatshqip.tosk.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.cheatshqip.tosk.chip.tokens.ToskChipColor
import com.cheatshqip.tosk.chip.tokens.ToskChipSize
import com.cheatshqip.tosk.chip.tokens.ToskChipTextStyle
import com.cheatshqip.tosk.tokens.semantic.ToskShape

@Composable
fun ToskChip(
    modifier: Modifier = Modifier,
    size: ToskChipSize = ToskChipSize.Medium,
    color: ToskChipColor = ToskChipColor.default(),
    enabled: Boolean = true,
    selected: Boolean = false,
    contentDescription: String,
    onClick: () -> Unit = {},
    label: @Composable (() -> Unit),
) {
    var active by remember { mutableStateOf(selected) }

    CompositionLocalProvider(LocalRippleConfiguration provides color.ripple) {
        InputChip(
            enabled = enabled,
            modifier = modifier
                .requiredHeight(size.minHeight)
                .clearAndSetSemantics {
                    this.contentDescription = contentDescription
                },
            shape = ToskShape.Medium,
            colors = selectableChipColorsFrom(color),
            label = {
                CompositionLocalProvider(LocalTextStyle provides ToskChipTextStyle.default) {
                    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
                        label()
                    }
                }
            },
            elevation = null,
            border = InputChipDefaults.inputChipBorder(
                enabled = enabled,
                selected = active,
                borderColor = color.borderColor,
                selectedBorderColor = color.selectedBorderColor,
                selectedBorderWidth = size.borderSize
            ),
            selected = active,
            onClick = {
                active = !active
                onClick()
            }
        )
    }
}

@Composable
private fun selectableChipColorsFrom(
    color: ToskChipColor,
): SelectableChipColors = SelectableChipColors(
    containerColor = color.backgroundColor,
    labelColor = color.contentColor,
    leadingIconColor = color.contentColor,
    trailingIconColor = color.contentColor,
    selectedContainerColor = color.selectedBackgroundColor,
    selectedLabelColor = color.selectedContentColor,
    selectedLeadingIconColor = color.selectedContentColor,
    selectedTrailingIconColor = color.selectedContentColor,
    disabledLabelColor = color.contentColor,
    disabledTrailingIconColor = color.contentColor,
    disabledLeadingIconColor = color.contentColor,
    disabledContainerColor = color.backgroundColor,
    disabledSelectedContainerColor = color.backgroundColor,
)