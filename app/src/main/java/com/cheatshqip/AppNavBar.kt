package com.cheatshqip

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.cheatshqip.tosk.navbar.ToskNavBar
import com.cheatshqip.tosk.navbar.ToskNavBarItem

@Composable
fun AppNavBar(
    currentDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
) {
    ToskNavBar {
        for (destination in AppDestination.entries) {
            ToskNavBarItem(
                modifier = Modifier.testTag("nav_${destination.route}"),
                selected = destination == currentDestination,
                contentDescription = stringResource(destination.contentDescriptionRes),
                onClick = { onDestinationSelected(destination) },
                icon = { Icon(imageVector = destination.icon, contentDescription = null) },
                label = { Text(stringResource(destination.labelRes)) },
            )
        }
    }
}
