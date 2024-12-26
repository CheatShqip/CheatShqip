package com.cheatshqip

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import com.cheatshqip.ui.theme.CheatShqipTheme
import com.cheatshqip.ui.theme.spacing
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    innerPadding: PaddingValues = PaddingValues(spacing().NONE),
    homeScreenViewModel: HomeScreenViewModel = koinViewModel()
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(spacing().SMALL),
        verticalArrangement = Arrangement.spacedBy(spacing().SMALL)
    ) {
        val containerModifier = Modifier.fillMaxWidth()

        TextField(
            modifier = containerModifier,
            value = homeScreenViewModel.homeScreenUIState.value.search,
            onValueChange = { homeScreenViewModel.onSearchChanged(it) },
            label = { Text("Word") }
        )
        Button(
            modifier = containerModifier,
            onClick = { homeScreenViewModel.onSearch() }
        ) {
            Text("Translate")
        }

        when (val homeScreenUIState = homeScreenViewModel.homeScreenUIState.value) {
            is HomeScreenUIState.WithSearchAndWithTranslationSuggestions -> {
                HorizontalDivider(
                    modifier = containerModifier
                        .background(MaterialTheme.colorScheme.primary)
                )

                for (translation in homeScreenUIState.translationSuggestions) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(
                                onClick = { /*onTranslationClicked(translation)*/ },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple()
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = translation.value
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "Arrow",
                            colorFilter = tint(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }

            else -> {
                // do nothing
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CheatShqipTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(LocalContext.current.resources.getString(R.string.app_name))
                    }
                )
            },
        ) { innerPadding ->
            HomeScreen(
                innerPadding = innerPadding,
                homeScreenViewModel = homeScreenViewModelWithSearchAndTranslations()
            )
        }
    }
} // TODO : 1. Add a preview for HomeScreen with loading state
// TODO : 2. Add a preview for HomeScreen with error state
// TODO : 3. Add a preview for HomeScreen with empty state

private fun homeScreenViewModelWithSearchAndTranslations() = HomeScreenViewModel(
    coroutineDispatcher = Dispatchers.Default,
    getWordTranslationSuggestionsUseCase = object : GetWordTranslationSuggestionsUseCase {
        override suspend fun getWorldTranslationSuggestions(word: Word): List<Translation> {
            return listOf(
                Translation("test")
            )
        }
    },
    homeScreenUIState = mutableStateOf(
        HomeScreenUIState.WithSearchAndWithTranslationSuggestions(
            search = "test",
            translationSuggestions = listOf(
                Translation("test"), Translation("test2")
            )
        )
    )
)