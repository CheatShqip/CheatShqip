package com.cheatshqip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheatshqip.ui.theme.CheatShqipTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    homeScreenViewModel: HomeScreenViewModel = koinViewModel()
) {
    Column(modifier = Modifier.padding(innerPadding), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = homeScreenViewModel.homeScreenUIState.value.search,
            onValueChange = { homeScreenViewModel.onSearchChanged(it) },
            label = { Text("Word") }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { homeScreenViewModel.onSearch() }

        ) {
            Text("Translate")
        }

        when (val homeScreenUIState = homeScreenViewModel.homeScreenUIState.value) {
            is HomeScreenUIState.WithSearchAndWithTranslationSuggestions -> {
                for (translation in homeScreenUIState.translationSuggestions) {
                    Text(text = translation.value)
                }
            }

            else -> {
                // do nothing
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CheatShqipTheme {
        HomeScreen()
    }
}