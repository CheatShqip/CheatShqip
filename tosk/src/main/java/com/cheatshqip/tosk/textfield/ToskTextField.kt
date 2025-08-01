package com.cheatshqip.tosk.textfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.tokens.primitive.ToskBorderSize
import com.cheatshqip.tosk.tokens.primitive.ToskPalette
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
) {
    val colors = TextFieldDefaults.colors(
        errorIndicatorColor = ToskPalette.crimson,
        errorContainerColor = ToskTheme.colors.background.secondary,
        errorTextColor = ToskTheme.colors.text.primary,
        focusedIndicatorColor = ToskTheme.colors.border.accent,
        focusedContainerColor = ToskTheme.colors.background.secondary,
        focusedTextColor = ToskTheme.colors.text.primary,
        unfocusedIndicatorColor = ToskTheme.colors.border.secondary,
        unfocusedContainerColor = ToskTheme.colors.background.secondary,
        unfocusedTextColor = ToskTheme.colors.text.primary,
        disabledIndicatorColor = ToskTheme.colors.border.secondary,
        disabledContainerColor = ToskTheme.colors.background.secondary,
        disabledTextColor = ToskTheme.colors.text.primary, // TODO pour disabled
    )
    val interactionSource = remember { MutableInteractionSource() }

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier =
                modifier
                    .defaultMinSize(
                        minWidth = OutlinedTextFieldDefaults.MinWidth,
                        minHeight = OutlinedTextFieldDefaults.MinHeight
                    ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = ToskTheme.typography.label2,
            cursorBrush = SolidColor(ToskTheme.colors.text.primary),
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
                        colors = colors,
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = colors,
                                shape = ToskShape.Medium,
                                unfocusedBorderThickness = ToskBorderSize.Medium,
                                focusedBorderThickness = ToskBorderSize.Medium,
                            )
                        }
                    )
                }
        )
    }
}