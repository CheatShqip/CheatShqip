package com.cheatshqip

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
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

        composeTestRule.onNodeWithText("kartë").assertIsDisplayed()
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
        composeTestRule.onNodeWithText("kár/të,-ta").assertIsDisplayed()
    }

    @Test
    fun wordDetailScreen_displaysGrammaticalDisplayPluralForm() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithText("Plural").assertIsDisplayed()
        composeTestRule.onNodeWithText("kártat").assertIsDisplayed()
    }

    @Test
    fun wordDetailScreen_declensions_displaysAllLabels() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithText("Declensions").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nominative").assertIsDisplayed()
        composeTestRule.onNodeWithText("Genitive").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dative").assertIsDisplayed()
        composeTestRule.onNodeWithText("Accusative").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ablative").assertIsDisplayed()
    }

    @Test
    fun wordDetailScreen_declensions_displaysValues() {
        navigateToKarteDetail()

        composeTestRule.onNodeWithText("karta").assertIsDisplayed()
        composeTestRule.onNodeWithText("kartën").assertIsDisplayed()
        // Genitive, Dative, and Ablative all share the same value
        composeTestRule.onAllNodesWithText("kartës")[0].assertIsDisplayed()
    }

    private fun navigateToKarteDetail() {
        composeTestRule.onNodeWithText("Word").performTextInput("card")
        composeTestRule.onNodeWithContentDescription(label = "Translate").performClick()
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule.onAllNodesWithText("kartë").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("kartë").performClick()
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule.onAllNodesWithText("Declensions").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
