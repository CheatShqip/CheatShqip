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
class NavBarTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navBar_declineTab_showsDeclineScreen() {
        composeTestRule.onNodeWithContentDescription("Decline tab").performClick()

        composeTestRule.onNodeWithText("Albanian word").assertIsDisplayed()
    }

    @Test
    fun navBar_conjugateTab_showsConjugateScreen() {
        composeTestRule.onNodeWithContentDescription("Conjugate tab").performClick()

        composeTestRule.onNodeWithText("Coming soon").assertIsDisplayed()
    }

    @Test
    fun navBar_translateTab_showsHomeScreen() {
        composeTestRule.onNodeWithContentDescription("Decline tab").performClick()

        composeTestRule.onNodeWithContentDescription("Translate tab").performClick()

        composeTestRule.onNodeWithText("Word").assertIsDisplayed()
    }

    @Test
    fun navBar_survivesNavigationToWordDetail() {
        composeTestRule.onNodeWithText("Word").performTextInput("paper")
        composeTestRule.onNodeWithContentDescription(label = "Translate").performClick()
        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("kartë")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("kartë").performClick()

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule.onAllNodesWithText("Declensions")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithContentDescription("Decline tab").assertIsDisplayed()
    }
}
