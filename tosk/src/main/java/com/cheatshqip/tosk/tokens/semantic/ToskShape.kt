package com.cheatshqip.tosk.tokens.semantic

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import com.cheatshqip.tosk.tokens.primitive.ToskBorderRadius

sealed class ToskShape : Shape {
    data object Medium : ToskShape(), Shape by RoundedCornerShape(ToskBorderRadius.Medium)
}