package com.cheatshqip.tosk.tokens

import androidx.compose.material3.ButtonColors
import androidx.compose.ui.graphics.Color
import kotlin.Boolean

sealed class ToskColors(
    val textPrimary: Color,
    val textSecondary: Color,
    val textAccent: Color,
    val textHolorRed: Color,
    val textHoloBlue: Color,
    val textHoloGreen: Color,
    val textHoloYellow: Color,
    val textHoloOrange: Color,
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundAccent: Color,
    val backgroundHoloRed: Color,
    val backgroundHoloBlue: Color,
    val backgroundHoloGreen: Color,
    val backgroundHoloYellow: Color,
    val backgroundHoloOrange: Color,
    val borderPrimary: Color,
    val borderSecondary: Color,
    val borderAccent: Color,
) {
    data object Light : ToskColors(
        textPrimary = Color(0xFF000000),
        textSecondary = Color(0xFF4B5563),
        textAccent = Color(0xFF7E22CE),
        textHolorRed = Color(0xFFB91C1B),
        textHoloBlue = Color(0xFF1C4ED8),
        textHoloGreen = Color(0xFF17803D),
        textHoloYellow = Color(0xFFA16207),
        textHoloOrange = Color(0xFFC2410B),
        backgroundPrimary = Color(0xFFDC2625),
        backgroundSecondary = Color(0xFFF3F4F6),
        backgroundAccent = Color(0xFFF3E8FF),
        backgroundHoloRed = Color(0xFFFEE2E1),
        backgroundHoloBlue = Color(0xFFDBE9FE),
        backgroundHoloGreen = Color(0xFFDCFCE7),
        backgroundHoloYellow = Color(0xFFFEF9C3),
        backgroundHoloOrange = Color(0xFFFFEDD5),
        borderPrimary = Color(0xFFFAD7D5),
        borderSecondary = Color(0xFFE5E7EB),
        borderAccent = Color(0XFFD8B4FE)
    )

    data object Dark : ToskColors(
        textPrimary = TODO(),
        textSecondary = TODO(),
        textAccent = TODO(),
        textHolorRed = TODO(),
        textHoloBlue = TODO(),
        textHoloGreen = TODO(),
        textHoloYellow = TODO(),
        textHoloOrange = TODO(),
        backgroundPrimary = TODO(),
        backgroundSecondary = TODO(),
        backgroundAccent = TODO(),
        backgroundHoloRed = TODO(),
        backgroundHoloBlue = TODO(),
        backgroundHoloGreen = TODO(),
        backgroundHoloYellow = TODO(),
        backgroundHoloOrange = TODO(),
        borderPrimary = TODO(),
        borderSecondary = TODO(),
        borderAccent = TODO()
    )
}
