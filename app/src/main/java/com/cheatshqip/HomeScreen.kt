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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheatshqip.application.port.input.GetWordTranslationSuggestionsUseCase
import com.cheatshqip.domain.Translation
import com.cheatshqip.domain.Word
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.button.ToskButton
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing
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

        TextField(
            modifier = containerModifier,
            value = searchInput,
            onValueChange = { searchInput = it },
            label = { Text("Word") }
        )
        ToskButton(
            modifier = containerModifier,
            contentDescription = "Translate",
            onClick = { onSearch(searchInput) }
        ) {
            Text("Translate")
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
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
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
                colorFilter = tint(ToskTheme.colors.background.primary)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ToskTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = ToskTheme.colors.background.secondary,
                            titleContentColor = ToskTheme.colors.text.textOnPrimary,
                        ),
                    title = {
                        Text(LocalContext.current.resources.getString(R.string.app_name))
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
