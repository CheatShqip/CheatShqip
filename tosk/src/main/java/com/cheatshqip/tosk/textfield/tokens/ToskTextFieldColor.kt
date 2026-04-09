package com.cheatshqip.tosk.textfield.tokens

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme

data class ToskTextFieldColor(
    val errorIndicatorColor: Color,
    val errorContainerColor: Color,
    val errorTextColor: Color,
    val focusedIndicatorColor: Color,
    val focusedContainerColor: Color,
    val focusedTextColor: Color,
    val unfocusedIndicatorColor: Color,
    val unfocusedContainerColor: Color,
    val unfocusedTextColor: Color,
    val disabledIndicatorColor: Color,
    val disabledContainerColor: Color,
    val disabledTextColor: Color,
    val textSelectionHandleColor: Color,
    val textSelectionBackgroundColor: Color,
    val cursorBrushColor: Color,
) {
    @Composable
    fun textFieldColors(): TextFieldColors {
        return TextFieldDefaults.colors(
            errorCursorColor = errorIndicatorColor,
            errorContainerColor = errorContainerColor,
            errorTextColor = errorTextColor,
            focusedContainerColor = focusedContainerColor,
            focusedTextColor = focusedTextColor,
            unfocusedContainerColor = unfocusedContainerColor,
            unfocusedTextColor = focusedTextColor,
            disabledContainerColor = disabledContainerColor,
            disabledTextColor = focusedTextColor,
            cursorColor = cursorBrushColor,
            focusedPlaceholderColor = focusedTextColor,
            unfocusedPlaceholderColor = focusedTextColor,
            disabledPlaceholderColor = focusedTextColor,
            focusedSupportingTextColor = focusedTextColor,
            unfocusedSupportingTextColor = focusedTextColor,
            disabledSupportingTextColor = focusedTextColor,
            errorSupportingTextColor = focusedTextColor,
            focusedLabelColor = focusedTextColor,
            disabledLabelColor = focusedTextColor,
        )
    }

    @Composable
    fun textSelectionColors(): TextSelectionColors {
        return TextSelectionColors(
            handleColor = textSelectionHandleColor,
            backgroundColor = textSelectionBackgroundColor,
        )
    }

    companion object Companion {
        @Composable
        fun default(): ToskTextFieldColor {
            return ToskTextFieldColor(
                errorIndicatorColor = ToskTheme.colors.text.error,
                errorContainerColor = ToskTheme.colors.background.secondary,
                errorTextColor = ToskTheme.colors.text.error,
                focusedIndicatorColor = ToskTheme.colors.border.accent,
                focusedContainerColor = ToskTheme.colors.background.secondary,
                focusedTextColor = ToskTheme.colors.text.primary,
                unfocusedIndicatorColor = ToskTheme.colors.border.secondary,
                unfocusedContainerColor = ToskTheme.colors.background.secondary,
                unfocusedTextColor = ToskTheme.colors.text.primary,
                disabledIndicatorColor = ToskTheme.colors.border.secondary,
                disabledContainerColor = ToskTheme.colors.background.secondary,
                disabledTextColor = ToskTheme.colors.text.primary,
                textSelectionHandleColor = ToskTheme.colors.text.primary,
                textSelectionBackgroundColor = ToskTheme.colors.text.primary.copy(alpha = 0.4f),
                cursorBrushColor = ToskTheme.colors.text.primary, // TODO pour disabled
            )
        }
    }
}
