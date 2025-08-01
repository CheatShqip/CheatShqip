package com.cheatshqip.tosk.textfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.textfield.tokens.ToskTextFieldColors
import com.cheatshqip.tosk.tokens.primitive.ToskBorderSize
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
    colors: ToskTextFieldColors = ToskTextFieldColors.default()
) {
    val materialColors = TextFieldDefaults.colors(
        errorIndicatorColor = colors.errorIndicatorColor,
        errorContainerColor = colors.errorContainerColor,
        errorTextColor = colors.errorTextColor,
        focusedIndicatorColor = colors.focusedIndicatorColor,
        focusedContainerColor = colors.focusedContainerColor,
        focusedTextColor = colors.focusedTextColor,
        unfocusedIndicatorColor = colors.unfocusedIndicatorColor,
        unfocusedContainerColor = colors.unfocusedContainerColor,
        unfocusedTextColor = colors.unfocusedTextColor,
        disabledIndicatorColor = colors.disabledIndicatorColor,
        disabledContainerColor = colors.disabledContainerColor,
        disabledTextColor = colors.disabledTextColor,
    )
    val interactionSource = remember { MutableInteractionSource() }
    val textSelectionColors = TextSelectionColors(
        handleColor = ToskTheme.colors.text.primary,
        backgroundColor = ToskTheme.colors.text.primary.copy(alpha = 0.4f)
    )

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
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
                        colors = materialColors,
                        container = {
                            OutlinedTextFieldDefaults.Container(
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = materialColors,
                                shape = ToskShape.Medium,
                                unfocusedBorderThickness = ToskBorderSize.M,
                                focusedBorderThickness = ToskBorderSize.M,
                            )
                        }
                    )
                }
        )
    }
}