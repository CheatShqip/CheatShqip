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
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeScreen_topAppBar_displaysCorrectTitle() {
        composeTestRule.onNodeWithText("CheatShqip").assertIsDisplayed()
    }

    @Test
    fun homeScreen_translatePaper_displaysKarte() {
        composeTestRule.onNodeWithText("Word").performTextInput("paper")

        composeTestRule.onNodeWithContentDescription(label = "Translate").performClick()

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("kartë")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("kartë").assertIsDisplayed()
    }

    @Test
    fun homeScreen_unknownWord_displaysNoTranslationMessage() {
        composeTestRule.onNodeWithText("Word").performTextInput("zzzzzz")

        composeTestRule.onNodeWithContentDescription(label = "Translate").performClick()

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("No translation found.")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("No translation found.").assertIsDisplayed()
    }

    @Test
    fun homeScreen_moonlight_displaysDriteHene() {
        composeTestRule.onNodeWithText("Word").performTextInput("moonlight")

        composeTestRule.onNodeWithContentDescription(label = "Translate").performClick()

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("dritë hëne")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("dritë hëne").assertIsDisplayed()
    }
}
