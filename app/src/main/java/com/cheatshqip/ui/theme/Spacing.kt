package com.cheatshqip.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cheatshqip.R

data class Spacing(private val context: Context) {
    val XSMALL = context.resources.getDimension(R.dimen.spacing_x_small).dp
    val SMALL = context.resources.getDimension(R.dimen.spacing_small).dp
    val MEDIUM = context.resources.getDimension(R.dimen.spacing_medium).dp
    val LARGE = context.resources.getDimension(R.dimen.spacing_large).dp
    val XLARGE = context.resources.getDimension(R.dimen.spacing_x_large).dp
}

@Composable
fun spacing(): Spacing {
    return Spacing(LocalContext.current)
}