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
    fun homeScreen_translateCard_displaysKarte() {
        composeTestRule.onNodeWithText("Word").performTextInput("card")

        composeTestRule.onNodeWithContentDescription(label = "Translate").performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule.onAllNodesWithText("kartë")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("kartë").assertIsDisplayed()
    }
}
