package com.cheatshqip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheatshqip.domain.AblativeDeclension
import com.cheatshqip.domain.AccusativeDeclension
import com.cheatshqip.domain.AlbanianDeclensions
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.domain.DativeDeclension
import com.cheatshqip.domain.GenitiveDeclension
import com.cheatshqip.domain.GrammaticalDisplay
import com.cheatshqip.domain.NominativeDeclension
import com.cheatshqip.domain.NounParadigm
import com.cheatshqip.domain.PluralGrammaticalDisplay
import com.cheatshqip.domain.SingularGrammaticalDisplay
import com.cheatshqip.domain.Word
import com.cheatshqip.domain.WordGender
import com.cheatshqip.domain.WordKind
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.button.ToskButton
import com.cheatshqip.tosk.textfield.ToskTextField
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing
import com.cheatshqip.tosk.topappbar.ToskTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeclineScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: DeclineScreenViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DeclineScreen(
        state = state,
        onSearchChanged = viewModel::onSearchChanged,
        onSearch = viewModel::onSearch,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeclineScreen(
    state: DeclineScreenUIState,
    onSearchChanged: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = ToskTheme.colors.background.secondary,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal),
        topBar = {
            ToskTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
            )
        },
    ) { innerPadding ->
        DeclineScreenContent(
            state = state,
            innerPadding = innerPadding,
            onSearchChanged = onSearchChanged,
            onSearch = onSearch,
        )
    }
}

@Composable
private fun DeclineScreenContent(
    state: DeclineScreenUIState,
    innerPadding: PaddingValues,
    onSearchChanged: (String) -> Unit,
    onSearch: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(ToskSpacing.S),
        verticalArrangement = Arrangement.spacedBy(ToskSpacing.S),
    ) {
        val containerModifier = Modifier.fillMaxWidth()

        ToskTextField(
            modifier = containerModifier,
            value = state.search,
            onValueChange = onSearchChanged,
            placeholder = { Text(stringResource(R.string.decline_search_placeholder)) },
        )

        ToskButton(
            modifier = containerModifier,
            contentDescription = stringResource(R.string.decline_action),
            onClick = onSearch,
        ) {
            Text(stringResource(R.string.decline_action))
        }

        when (state) {
            is DeclineScreenUIState.Loaded -> WordDetailContent(wordDetail = state.wordDetail)
            is DeclineScreenUIState.NotFound -> Text(text = stringResource(R.string.decline_not_found))
            is DeclineScreenUIState.Error -> Text(text = stringResource(R.string.word_detail_error))
            is DeclineScreenUIState.Loading, is DeclineScreenUIState.Initial -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun DeclineScreenPreview() {
    ToskTheme {
        DeclineScreen(
            state = DeclineScreenUIState.Loaded(
                search = "kartë",
                wordDetail = AlbanianWordDetail(
                    word = Word("kartë"),
                    kind = WordKind.Name,
                    gender = WordGender.Feminine,
                    grammaticalDisplay = GrammaticalDisplay(
                        singular = SingularGrammaticalDisplay("kár/të,-ta"),
                        plural = PluralGrammaticalDisplay("kártat"),
                    ),
                    declensions = NounParadigm(
                        singularIndefinite = AlbanianDeclensions(
                            nominative = NominativeDeclension("kartë"),
                            genitive = GenitiveDeclension("karte"),
                            dative = DativeDeclension("karte"),
                            accusative = AccusativeDeclension("kartë"),
                            ablative = AblativeDeclension("karte"),
                        ),
                        singularDefinite = AlbanianDeclensions(
                            nominative = NominativeDeclension("karta"),
                            genitive = GenitiveDeclension("kartës"),
                            dative = DativeDeclension("kartës"),
                            accusative = AccusativeDeclension("kartën"),
                            ablative = AblativeDeclension("kartës"),
                        ),
                        pluralIndefinite = AlbanianDeclensions(
                            nominative = NominativeDeclension("karta"),
                            genitive = GenitiveDeclension("kartave"),
                            dative = DativeDeclension("kartave"),
                            accusative = AccusativeDeclension("karta"),
                            ablative = AblativeDeclension("kartave"),
                        ),
                        pluralDefinite = AlbanianDeclensions(
                            nominative = NominativeDeclension("kartat"),
                            genitive = GenitiveDeclension("kartave"),
                            dative = DativeDeclension("kartave"),
                            accusative = AccusativeDeclension("kartat"),
                            ablative = AblativeDeclension("kartave"),
                        ),
                    ),
                ),
            ),
            onSearchChanged = {},
            onSearch = {},
        )
    }
}
