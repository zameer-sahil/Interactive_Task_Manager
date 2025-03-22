package com.znextapp.interactivetaskmanager.tests

import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import com.znextapp.interactivetaskmanager.MainActivity
import com.znextapp.interactivetaskmanager.ui.screens.AddTaskScreen
import com.znextapp.interactivetaskmanager.ui.screens.TaskListScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SortingFilteringTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private lateinit var navController: TestNavHostController

    @Test
    fun verifySortingFunctionality() {
        composeTestRule.setContent {
            TaskListScreen(navController, hiltViewModel())

        }

        // Perform sorting action (Assume a button with "Sort" exists)
        composeTestRule.onNodeWithText("Sort").performClick()

        // Verify if the first item is updated according to sorting
        composeTestRule.onNodeWithTag("list_item_0") // Replace with actual tag
            .assertExists()
            .assertTextContains("Expected First Item") // Replace with expected sorted item
    }

    @Test
    fun verifyFilteringFunctionality() {
        composeTestRule.setContent {
            TaskListScreen(navController, hiltViewModel())
        }

        // Enter text in the filter field (Assume a TextField with a testTag)
        composeTestRule.onNodeWithTag("filter_input").performTextInput("keyword")

        // Verify if filtered results are displayed
        composeTestRule.onAllNodesWithTag("list_item").assertCountEquals(3) // Change count accordingly
    }


    @Test
    fun verifyAnimationTriggers() {
        composeTestRule.setContent {
            TaskListScreen(navController, hiltViewModel())
        }

        composeTestRule.onNodeWithTag("animate_button").performClick()

        // Wait for animation to complete
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            composeTestRule.onNodeWithTag("animated_view").fetchSemanticsNode() != null
        }


        composeTestRule.onNodeWithTag("animated_view").assertExists().assertIsDisplayed()
    }

    @Test
    fun captureScreenshot_lightMode() {
        composeTestRule.setContent {
            AddTaskScreen(navController = rememberNavController())
        }
    }

    @Test
    fun captureScreenshot_darkMode() {
        composeTestRule.setContent {
            AddTaskScreen(navController = rememberNavController())
        }
    }
}
