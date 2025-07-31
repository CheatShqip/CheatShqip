package com.cheatshqip.tosk.textfield

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.tokens.primitive.ToskPalette
import com.cheatshqip.tosk.tokens.semantic.ToskShape

@Composable
fun ToskTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.clipToBounds(),
        textStyle = ToskTheme.typography.label2,
        placeholder = placeholder,
        shape = ToskShape.Medium,
        colors = TextFieldDefaults.colors(
            errorIndicatorColor = ToskPalette.transparent,
            focusedIndicatorColor = ToskPalette.transparent,
            unfocusedIndicatorColor = ToskPalette.transparent,
            disabledIndicatorColor = ToskPalette.transparent,
            unfocusedContainerColor = ToskTheme.colors.background.secondary,
            focusedContainerColor = ToskTheme.colors.background.secondary,
            disabledContainerColor = ToskTheme.colors.background.secondary,
            errorContainerColor = ToskTheme.colors.background.secondary,
            focusedTextColor = ToskTheme.colors.text.primary,
            unfocusedTextColor = ToskTheme.colors.text.primary,
            disabledTextColor = ToskTheme.colors.text.primary,
            errorTextColor = ToskTheme.colors.text.primary,
        )
    )
}