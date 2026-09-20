package com.cheatshqip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cheatshqip.domain.AlbanianDeclensions
import com.cheatshqip.domain.AlbanianWordDetail
import com.cheatshqip.tosk.ToskTheme
import com.cheatshqip.tosk.chip.ToskChip
import com.cheatshqip.tosk.tokens.primitive.ToskSpacing

@Composable
fun WordDetailContent(
    wordDetail: AlbanianWordDetail,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(ToskSpacing.S),
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
    val paradigm = wordDetail.declensions
    Column(verticalArrangement = Arrangement.spacedBy(ToskSpacing.S)) {
        Text(
            text = stringResource(R.string.declensions),
            style = ToskTheme.typography.label2,
        )
        DeclensionSetSection(
            title = stringResource(R.string.singular_indefinite),
            declensions = paradigm.singularIndefinite,
        )
        DeclensionSetSection(
            title = stringResource(R.string.singular_definite),
            declensions = paradigm.singularDefinite,
        )
        DeclensionSetSection(
            title = stringResource(R.string.plural_indefinite),
            declensions = paradigm.pluralIndefinite,
        )
        DeclensionSetSection(
            title = stringResource(R.string.plural_definite),
            declensions = paradigm.pluralDefinite,
        )
    }
}

@Composable
private fun DeclensionSetSection(title: String, declensions: AlbanianDeclensions) {
    Column(verticalArrangement = Arrangement.spacedBy(ToskSpacing.XS)) {
        Text(text = title, style = ToskTheme.typography.label2)
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
