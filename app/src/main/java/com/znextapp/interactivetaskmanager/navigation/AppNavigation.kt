package com.znextapp.interactivetaskmanager.ui.navigation

import TaskDetailScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.znextapp.interactivetaskmanager.ui.screens.AddTaskScreen
import com.znextapp.interactivetaskmanager.ui.screens.SettingsScreen
import com.znextapp.interactivetaskmanager.ui.screens.TaskListScreen
import com.znextapp.interactivetaskmanager.viewmodel.SettingsViewModel

@Composable
fun AppNavigation(settingsViewModel: SettingsViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") {
            TaskListScreen(navController)
        }
        composable("add_task") {
            AddTaskScreen(navController)
        }
        composable("task_detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: -1
            if (taskId != -1) {
                TaskDetailScreen(taskId, navController)
            }
        }
        composable("settings") {
            SettingsScreen(settingsViewModel, navController)
        }
    }
}

