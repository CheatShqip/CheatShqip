package com.cheatshqip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.button.ToskButton
import com.cheatshqip.tosk.card.ToskCard
import com.cheatshqip.tosk.textfield.ToskTextField
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing
import com.cheatshqip.tosk.topappbar.ToskTopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoute(
    innerPadding: PaddingValues = PaddingValues(ToskTheme.spacing.None),
    homeScreenViewModel: HomeScreenViewModel = koinViewModel()
) {
    val homeScreenUIState: HomeScreenUIState by homeScreenViewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        homeScreenUIState,
        innerPadding,
        homeScreenViewModel::onSearch
    )
}

@Composable
private fun HomeScreen(
    homeScreenUIState: HomeScreenUIState,
    innerPadding: PaddingValues,
    onSearch: (String) -> Unit = {},
) {
    var searchInput by remember { mutableStateOf("") }

    Column(
        modifier =
            Modifier
                .padding(innerPadding)
                .padding(ToskSpacing.S),
        verticalArrangement = Arrangement.spacedBy(ToskSpacing.S)
    ) {
        val containerModifier = Modifier.fillMaxWidth()

        ToskTextField(
            modifier = containerModifier,
            value = searchInput,
            onValueChange = { searchInput = it },
            placeholder = { Text("Word") }
        )

        ToskButton(
            modifier = containerModifier,
            contentDescription = stringResource(R.string.translate),
            onClick = { onSearch(searchInput) }
        ) {
            Text(stringResource(R.string.translate))
        }

        if (homeScreenUIState is HomeScreenUIState.WithTranslationSuggestions) {
            TranslationSuggestions(containerModifier, homeScreenUIState.translationSuggestions)
        }
    }
}

@Composable
private fun TranslationSuggestions(
    modifier: Modifier,
    translationSuggestions: List<Translation>
) {
    HorizontalDivider(
        modifier =
            modifier
                .background(ToskTheme.colors.background.primary)
    )

    for (translation in translationSuggestions) {
        ToskCard(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { /*onTranslationClicked(translation)*/ },
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple()
                    ),
            onClick = {},
            enabled = true,
        ) {
            Row(
                modifier = modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = translation.value
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ToskTheme {
        Scaffold(
            containerColor = ToskTheme.colors.background.secondary,
            topBar = {
                ToskTopAppBar(
                    title = {
                        Text(stringResource(R.string.app_name))
                    }
                )
            },
        ) { innerPadding ->
            HomeScreenRoute(
                innerPadding = innerPadding,
                homeScreenViewModel = homeScreenViewModelWithSearchAndTranslations()
            )
        }
    }
} // TODO : 1. Add a preview for HomeScreen with loading state
// TODO : 2. Add a preview for HomeScreen with error state
// TODO : 3. Add a preview for HomeScreen with empty state

private fun homeScreenViewModelWithSearchAndTranslations() =
    HomeScreenViewModel(
        coroutineDispatcher = Dispatchers.Default,
        getWordTranslationSuggestionsUseCase =
            object : GetWordTranslationSuggestionsUseCase {
                override suspend fun getWorldTranslationSuggestions(word: Word): List<Translation> {
                    return listOf(
                        Translation("test"),
                        Translation("test2")
                    )
                }
            },
        search = MutableStateFlow("test"),
    )
