package com.cheatshqip.toskdemo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.badge.ToskBadge
import com.cheatshqip.tosk.badge.tokens.ToskBadgeColor
import com.cheatshqip.tosk.button.ToskButton
import com.cheatshqip.tosk.button.tokens.ToskButtonColor
import com.cheatshqip.tosk.card.ToskCard
import com.cheatshqip.tosk.chip.ToskChip
import com.cheatshqip.tosk.navbar.ToskNavBar
import com.cheatshqip.tosk.navbar.ToskNavBarItem
import com.cheatshqip.tosk.textfield.ToskTextField
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ToskSpacing.M)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToskButton(
            enabled = true,
            color = ToskButtonColor.secondary(),
            contentDescription = "Singular Definite",
            onClick = { },
        ) { Text("Singular Definite") }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskButton(
            enabled = true,
            contentDescription = "Singular Indefinite",
            onClick = { },
        ) { Text("Singular Indefinite") }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskChip(
            enabled = true,
            contentDescription = "Singular Indefinite",
            onClick = { },
        ) { Text("Singular Indefinite") }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskTextField(
            modifier = Modifier,
            value = "Word",
            onValueChange = { },
            placeholder = { Text("Word") }
        )

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskTextField(
            modifier = Modifier,
            value = "Verb",
            onValueChange = { },
            placeholder = { Text("Verb") }
        )

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskCard(onClick = {}) {
            Text("Lol")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info1()
        ) {
            Text(text = "NOMINATIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info2()
        ) {
            Text(text = "ACCUSATIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info3()
        ) {
            Text(text = "GENITIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info4()
        ) {
            Text(text = "DATIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskBadge(
            color = ToskBadgeColor.info5()
        ) {
            Text(text = "ABLATIVE")
        }

        Spacer(modifier = Modifier.padding(ToskSpacing.M))

        ToskNavBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
            ToskNavBarItem(
                selected = true,
                contentDescription = "Translate tab",
                onClick = {},
                icon = { Icon(imageVector = Icons.Filled.Translate, contentDescription = null) },
                label = { Text("Translate") },
            )
            ToskNavBarItem(
                selected = false,
                contentDescription = "Decline tab",
                onClick = {},
                icon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = null) },
                label = { Text("Decline") },
            )
            ToskNavBarItem(
                selected = false,
                contentDescription = "Conjugate tab",
                onClick = {},
                icon = { Icon(imageVector = Icons.Filled.Sync, contentDescription = null) },
                label = { Text("Conjugate") },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ToskTheme {
        MainScreen()
    }
}