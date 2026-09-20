package com.cheatshqip

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WordDetailScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun wordDetailScreen_topAppBar_displaysWordAsTitle() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithTag("word_detail_top_bar_title").assertTextEquals("kartë")
    }

    @Test
    fun wordDetailScreen_backButton_navigatesBackToHome() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        composeTestRule.onNodeWithText("CheatShqip").assertIsDisplayed()
    }

    @Test
    fun wordDetailScreen_displaysKindAndGenderChips() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithContentDescription("Name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Feminine").assertIsDisplayed()
    }

    @Test
    fun wordDetailScreen_displaysGrammaticalDisplaySingularForm() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithText("Singular").assertIsDisplayed()
        // Singular display comes from the bundled DB compact form, e.g. "kart/ë,-a"
        composeTestRule.onNodeWithText("kart/ë,-a").assertIsDisplayed()
    }

    @Test
    fun wordDetailScreen_displaysGrammaticalDisplayPluralForm() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithText("Plural").assertIsDisplayed()
        // "karta" is also the singular definite nominative and plural indefinite
        // nominative/accusative declension values, so take the first node (plural display)
        composeTestRule.onAllNodesWithText("karta")[0].assertIsDisplayed()
    }

    @Test
    fun wordDetailScreen_declensions_displaysAllSetHeadersAndLabels() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithText("Declensions").assertIsDisplayed()
        composeTestRule.onNodeWithText("Singular Indefinite").assertIsDisplayed()
        composeTestRule.onNodeWithText("Singular Definite").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plural Indefinite").assertIsDisplayed()
        composeTestRule.onNodeWithText("Plural Definite").assertIsDisplayed()
        // Case labels repeat once per set (4 sets)
        composeTestRule.onAllNodesWithText("Nominative")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Genitive")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Dative")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Accusative")[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Ablative")[0].assertIsDisplayed()
    }

    @Test
    fun wordDetailScreen_declensions_displaysValues() {
        navigateToKarteDetail()

        // Singular definite nominative, plural indefinite nominative, and plural indefinite
        // accusative all share the value "karta"
        composeTestRule.onAllNodesWithText("karta")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("kartën").assertIsDisplayed()
        // Plural definite nominative and accusative both share the value "kartat"
        composeTestRule.onAllNodesWithText("kartat")[0].assertIsDisplayed()
        // Plural genitive, dative, and ablative (indefinite + definite) share "kartave"
        composeTestRule.onAllNodesWithText("kartave")[0].assertIsDisplayed()
        // Singular definite genitive, dative, and ablative all share the value "kartës"
        composeTestRule.onAllNodesWithText("kartës")[0].assertIsDisplayed()
    }

    private fun navigateToKarteDetail() {
        composeTestRule.onNodeWithText("Word").performTextInput("paper")
        composeTestRule.onNodeWithContentDescription(label = "Translate").performClick()
        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("kartë").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("kartë").performClick()
        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("Declensions").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
