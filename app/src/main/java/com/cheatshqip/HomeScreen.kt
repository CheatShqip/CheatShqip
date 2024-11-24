package com.cheatshqip

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cheatshqip.ui.theme.CheatShqipTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier.Companion, homeScreenViewModel: HomeScreenViewModel = koinViewModel()) {
    Text(
        text = homeScreenViewModel.greeting,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CheatShqipTheme {
        HomeScreen()
    }
}