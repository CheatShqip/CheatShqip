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
class DeclineScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun declineScreen_knownWord_displaysDeclensions() {
        composeTestRule.onNodeWithContentDescription("Decline tab").performClick()

        composeTestRule.onNodeWithText("Albanian word").performTextInput("karte")
        composeTestRule.onNodeWithContentDescription("Decline").performClick()

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("Nominative")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodesWithText("kartës")[0].assertIsDisplayed()
    }

    @Test
    fun declineScreen_unknownWord_displaysNotFoundMessage() {
        composeTestRule.onNodeWithContentDescription("Decline tab").performClick()

        composeTestRule.onNodeWithText("Albanian word").performTextInput("zzzznotaword")
        composeTestRule.onNodeWithContentDescription("Decline").performClick()

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("No declensions found for this word.")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }
}
