package com.znextapp.interactivetaskmanager.tests

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.znextapp.interactivetaskmanager.ui.screens.AddTaskScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AddTaskScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun addTaskScreen_allFieldsExist() {
        composeTestRule.setContent {
            AddTaskScreen(navController) // ViewModels are automatically injected
        }

        // Check if input fields exist
        composeTestRule.onNodeWithText("Task Title").assertExists()
        composeTestRule.onNodeWithText("Task Description").assertExists()
        composeTestRule.onNodeWithText("Priority").assertExists()
        composeTestRule.onNodeWithText("Due Date").assertExists()
        composeTestRule.onNodeWithText("Add Task").assertExists()
    }

    @Test
    fun addTaskScreen_taskCanBeAdded() {
        composeTestRule.setContent {
            AddTaskScreen(navController)
        }

        // Enter task details
        composeTestRule.onNodeWithText("Task Title").performTextInput("Test Task")
        composeTestRule.onNodeWithText("Task Description").performTextInput("This is a test task")

        // Open and select priority
        composeTestRule.onNodeWithText("Priority").performClick()
        composeTestRule.onNodeWithText("High").performClick()

        // Click Add Task
        composeTestRule.onNodeWithText("Add Task").performClick()

        // TODO: Add assertion to check if task is added (depending on your implementation)
    }

    @Test
    fun addTaskScreen_prioritySelectionWorks() {
        composeTestRule.setContent {
            AddTaskScreen(navController)
        }

        // Open priority dropdown
        composeTestRule.onNodeWithText("Priority").performClick()

        // Select "Medium" priority
        composeTestRule.onNodeWithText("Medium").performClick()

        // Assert selection
        composeTestRule.onNodeWithText("Medium").assertExists()
    }

    @Test
    fun addTaskScreen_dueDatePickerOpens() {
        composeTestRule.setContent {
            AddTaskScreen(navController)
        }

        // Open due date picker
        composeTestRule.onNodeWithText("Due Date").performClick()

        // TODO: Assert that the date picker opens (depends on implementation)
    }
}
