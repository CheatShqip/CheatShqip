package com.cheatshqip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.cheatshqip.domain.PluralGrammaticalDisplay
import com.cheatshqip.domain.SingularGrammaticalDisplay
import com.cheatshqip.domain.Word
import com.cheatshqip.domain.WordGender
import com.cheatshqip.domain.WordKind
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.chip.ToskChip
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing
import com.cheatshqip.tosk.topappbar.ToskTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun WordDetailScreenRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WordDetailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    WordDetailScreen(state = state, onBack = onBack, modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WordDetailScreen(
    state: WordDetailUIState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = ToskTheme.colors.background.secondary,
        topBar = {
            ToskTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                title = {
                    if (state is WordDetailUIState.Loaded) {
                        Text(state.wordDetail.word.value)
                    }
                },
            )
        },
    ) { innerPadding ->
        when (state) {
            is WordDetailUIState.Loading -> Unit
            is WordDetailUIState.Error -> Text(
                text = stringResource(R.string.word_detail_error),
                modifier = Modifier.padding(innerPadding).padding(ToskSpacing.S),
            )
            is WordDetailUIState.Loaded -> WordDetail(
                wordDetail = state.wordDetail,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun WordDetail(
    wordDetail: AlbanianWordDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(ToskSpacing.S),
        verticalArrangement = Arrangement.spacedBy(ToskSpacing.S),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(ToskSpacing.XS)) {
            ToskChip(
                contentDescription = wordDetail.kind.name,
                label = { Text(wordDetail.kind.name) },
            )
            ToskChip(
                contentDescription = wordDetail.gender.name,
                label = { Text(wordDetail.gender.name) },
            )
        }

        GrammaticalDisplaySection(wordDetail)

        HorizontalDivider()

        DeclensionsSection(wordDetail)
    }
}

@Composable
private fun GrammaticalDisplaySection(wordDetail: AlbanianWordDetail) {
    Column(verticalArrangement = Arrangement.spacedBy(ToskSpacing.XS)) {
        Text(
            text = stringResource(R.string.singular),
            style = ToskTheme.typography.label2,
        )
        Text(text = wordDetail.grammaticalDisplay.singular.value)
        Text(
            text = stringResource(R.string.plural),
            style = ToskTheme.typography.label2,
        )
        Text(text = wordDetail.grammaticalDisplay.plural.value)
    }
}

@Composable
private fun DeclensionsSection(wordDetail: AlbanianWordDetail) {
    val declensions = wordDetail.singularDefiniteDeclensions
    Column(verticalArrangement = Arrangement.spacedBy(ToskSpacing.XS)) {
        Text(
            text = stringResource(R.string.declensions),
            style = ToskTheme.typography.label2,
        )
        DeclensionRow(label = stringResource(R.string.nominative), value = declensions.nominative.value)
        DeclensionRow(label = stringResource(R.string.genitive), value = declensions.genitive.value)
        DeclensionRow(label = stringResource(R.string.dative), value = declensions.dative.value)
        DeclensionRow(label = stringResource(R.string.accusative), value = declensions.accusative.value)
        DeclensionRow(label = stringResource(R.string.ablative), value = declensions.ablative.value)
    }
}

@Composable
private fun DeclensionRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = ToskTheme.typography.label2)
        Text(text = value)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun WordDetailScreenPreview() {
    ToskTheme {
        WordDetailScreen(
            state = WordDetailUIState.Loaded(
                wordDetail = AlbanianWordDetail(
                    word = Word("kartë"),
                    kind = WordKind.Name,
                    gender = WordGender.Feminine,
                    grammaticalDisplay = GrammaticalDisplay(
                        singular = SingularGrammaticalDisplay("kár/të,-ta"),
                        plural = PluralGrammaticalDisplay("kártat"),
                    ),
                    singularDefiniteDeclensions = AlbanianDeclensions(
                        nominative = NominativeDeclension("karta"),
                        genitive = GenitiveDeclension("kartës"),
                        dative = DativeDeclension("kartës"),
                        accusative = AccusativeDeclension("kartën"),
                        ablative = AblativeDeclension("kartës"),
                    ),
                ),
            ),
            onBack = {},
        )
    }
}
