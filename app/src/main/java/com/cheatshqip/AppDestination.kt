package com.cheatshqip

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestination(
    val route: String,
    @StringRes val labelRes: Int,
    @StringRes val contentDescriptionRes: Int,
    val icon: ImageVector,
) {
    Translate(
        route = "translate",
        labelRes = R.string.nav_translate,
        contentDescriptionRes = R.string.nav_translate_content_description,
        icon = Icons.Filled.Translate,
    ),
    Decline(
        route = "decline",
        labelRes = R.string.nav_decline,
        contentDescriptionRes = R.string.nav_decline_content_description,
        icon = Icons.Filled.Edit,
    ),
    Conjugate(
        route = "conjugate",
        labelRes = R.string.nav_conjugate,
        contentDescriptionRes = R.string.nav_conjugate_content_description,
        icon = Icons.Filled.Sync,
    ),
}
