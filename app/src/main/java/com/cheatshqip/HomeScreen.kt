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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheatshqip.domain.Translation
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.button.ToskButton
import com.cheatshqip.tosk.card.ToskCard
import com.cheatshqip.tosk.textfield.ToskTextField
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing
import com.cheatshqip.tosk.topappbar.ToskTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(ToskTheme.spacing.None),
    homeScreenViewModel: HomeScreenViewModel = koinViewModel(),
) {
    val homeScreenUIState: HomeScreenUIState by homeScreenViewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        homeScreenUIState = homeScreenUIState,
        innerPadding = innerPadding,
        onSearchChanged = homeScreenViewModel::onSearchChanged,
        onSearch = homeScreenViewModel::onSearch,
        modifier = modifier,
    )
}

@Composable
private fun HomeScreen(
    homeScreenUIState: HomeScreenUIState,
    innerPadding: PaddingValues,
    onSearchChanged: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(innerPadding)
                .padding(ToskSpacing.S),
        verticalArrangement = Arrangement.spacedBy(ToskSpacing.S)
    ) {
        val containerModifier = Modifier.fillMaxWidth()

        if (homeScreenUIState is WithSearch) {
            ToskTextField(
                modifier = containerModifier,
                value = homeScreenUIState.search,
                onValueChange = onSearchChanged,
                placeholder = { Text("Word") }
            )

            ToskButton(
                modifier = containerModifier,
                contentDescription = stringResource(R.string.translate),
                onClick = { onSearch() }
            ) {
                Text(stringResource(R.string.translate))
            }

            if (homeScreenUIState is HomeScreenUIState.WithTranslationSuggestions) {
                TranslationSuggestions(containerModifier, homeScreenUIState.translationSuggestions)
            }
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
@PreviewLightDark
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
            HomeScreen(
                innerPadding = innerPadding,
                homeScreenUIState = HomeScreenUIState.WithTranslationSuggestions(
                    search = "test",
                    translationSuggestions = listOf(
                        Translation("test"),
                        Translation("test2")
                    )
                ),
                onSearch = {},
                onSearchChanged = {},
            )
        }
    }
}