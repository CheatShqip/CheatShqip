package com.cheatshqip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cheatshqip.ui.theme.CheatShqipTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier.Companion, homeScreenViewModel: HomeScreenViewModel = koinViewModel()) {
    when (val homeScreenUIState = homeScreenViewModel.homeScreenUIState.value) {
        is HomeScreenUIState.Init -> {
            Column(modifier.fillMaxSize()) {
                TextField(
                    modifier = modifier,
                    value = homeScreenUIState.search,
                    onValueChange = { homeScreenViewModel.onSearchChanged(it) },
                    label = { Text("Word") }
                )
                Button(
                    modifier = modifier,
                    onClick = { homeScreenViewModel.onSearch() }

                ) {
                    Text("Translate")
                }
            }
        }
        is HomeScreenUIState.Success -> {
            Column(modifier.fillMaxSize()) {
                for (translation in homeScreenUIState.name) {
                    Text(text = translation.value)
                }
            }
        }
        is HomeScreenUIState.Error -> TODO()
        is HomeScreenUIState.Loading -> TODO()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CheatShqipTheme {
        HomeScreen()
    }
}