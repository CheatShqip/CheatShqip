package com.cheatshqip.tosk

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
internal fun InteractionSource.animateAsColor(
    default: Color,
    pressed: Color = default,
    focused: Color = pressed,
): Color {
    if (default == pressed && default == focused) {
        return default
    }
    val isPressed by collectIsPressedAsState()
    val isFocused by collectIsFocusedAsState()
    val target = when {
        isPressed -> pressed
        isFocused -> focused
        else -> default
    }
    return animateColorAsState(target).value
} // TODO : tester par rapport à l'utilisation de ripple