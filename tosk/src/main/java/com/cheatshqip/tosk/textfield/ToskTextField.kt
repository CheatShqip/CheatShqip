package com.cheatshqip.tosk.textfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.cheatshqip.tosk.LocalToskShowCursor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.cheatshqip.tosk.textfield.tokens.ToskTextFieldColor
import com.cheatshqip.tosk.textfield.tokens.ToskTextFieldSize
import com.cheatshqip.tosk.textfield.tokens.ToskTextFieldTextStyle
import com.cheatshqip.tosk.tokens.semantic.ToskShape

@Composable
fun ToskTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    colors: ToskTextFieldColor = ToskTextFieldColor.default()
) {
    val materialColors = colors.textFieldColors()
    val interactionSource = remember { MutableInteractionSource() }
    val textSelectionColors = colors.textSelectionColors()

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        BasicTextField(
            value = value,
            modifier =
                modifier
                    .defaultMinSize(
                        minWidth = ToskTextFieldSize.minWidth,
                        minHeight = ToskTextFieldSize.minHeight,
                    ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = ToskTextFieldTextStyle.default.copy(
                color = materialColors.textColor(enabled, isError),
            ),
            cursorBrush = SolidColor(if (LocalToskShowCursor.current) colors.cursorBrushColor else Color.Transparent),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox =
                @Composable { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value,
                        visualTransformation = visualTransformation,
                        innerTextField = innerTextField,
                        placeholder = placeholder,
                        label = label,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        prefix = prefix,
                        suffix = suffix,
                        supportingText = supportingText,
                        singleLine = singleLine,
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = materialColors,
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = materialColors,
                                shape = ToskShape.Medium,
                                unfocusedBorderThickness = ToskTextFieldSize.borderSize,
                                focusedBorderThickness = ToskTextFieldSize.borderSize,
                            )
                        }
                    )
                }
        )
    }
}

private fun TextFieldColors.textColor(
    enabled: Boolean,
    isError: Boolean
): Color {
    return when {
        !enabled -> disabledTextColor
        isError -> errorTextColor
        else -> focusedTextColor
    }
}

@PreviewLightDark
@Composable
fun ToskTextFieldPreview() {
    ToskTextField(
        value = "Test",
        onValueChange = { },
    )
}
