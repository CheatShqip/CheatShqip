package com.cheatshqip.tosk.textfield.tokens

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.tokens.primitive.ToskPalette

data class ToskTextFieldColors(
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
    fun textFieldColors() : TextFieldColors {
        return TextFieldDefaults.colors(
            errorIndicatorColor = errorIndicatorColor,
            errorContainerColor = errorContainerColor,
            errorTextColor = errorTextColor,
            focusedIndicatorColor = focusedIndicatorColor,
            focusedContainerColor = focusedContainerColor,
            focusedTextColor = focusedTextColor,
            unfocusedIndicatorColor = unfocusedIndicatorColor,
            unfocusedContainerColor = unfocusedContainerColor,
            unfocusedTextColor = unfocusedTextColor,
            disabledIndicatorColor = disabledIndicatorColor,
            disabledContainerColor = disabledContainerColor,
            disabledTextColor = disabledTextColor,
        )
    }

    @Composable
    fun textSelectionColors() : TextSelectionColors {
        return TextSelectionColors(
            handleColor = textSelectionHandleColor,
            backgroundColor = textSelectionBackgroundColor,
        )
    }


    companion object {
        @Composable
        fun default(): ToskTextFieldColors {
            return ToskTextFieldColors(
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
                disabledTextColor = ToskTheme.colors.text.primary,
                textSelectionHandleColor = ToskTheme.colors.text.primary,
                textSelectionBackgroundColor = ToskTheme.colors.text.primary.copy(alpha = 0.4f),
                cursorBrushColor = ToskTheme.colors.text.primary, // TODO pour disabled
            )
        }
    }
}