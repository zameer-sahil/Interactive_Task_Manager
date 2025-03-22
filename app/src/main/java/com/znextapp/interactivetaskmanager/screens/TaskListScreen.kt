package com.znextapp.interactivetaskmanager.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.znextapp.interactivetaskmanager.enums.FilterOption
import com.znextapp.interactivetaskmanager.enums.SortOption
import com.znextapp.interactivetaskmanager.model.Task
import com.znextapp.interactivetaskmanager.ui.theme.InteractiveTaskManagerTheme
import com.znextapp.interactivetaskmanager.utils.fromHex
import com.znextapp.interactivetaskmanager.viewmodel.SettingsViewModel
import com.znextapp.interactivetaskmanager.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import org.burnoutcrew.reorderable.detectReorderAfterLongPress

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskListScreen(navController: NavController, viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isClicked) 1.7f else 1f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        finishedListener = { isClicked = false }
    )
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showProgress by remember { mutableStateOf(false) }

    val reorderState = rememberReorderableLazyListState(onMove = { from, to ->
        coroutineScope.launch { viewModel.moveTask(from.index, to.index) }
    })

    LaunchedEffect(tasks) {
        showProgress = true
        kotlinx.coroutines.delay(2000)
        showProgress = false
    }

    InteractiveTaskManagerTheme(settingsViewModel = settingsViewModel) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Task Manager") },
                    actions = {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Rounded.Sort, contentDescription = "Sort Tasks")
                        }
                        DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                            SortOption.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name.replace("_", " ")) },
                                    onClick = {
                                        viewModel.setSortOption(option)
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Rounded.FilterList, contentDescription = "Filter Tasks")
                        }
                        DropdownMenu(expanded = showFilterMenu, onDismissRequest = { showFilterMenu = false }) {
                            FilterOption.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name.replace("_", " ")) },
                                    onClick = {
                                        viewModel.setFilterOption(option)
                                        showFilterMenu = false
                                    }
                                )
                            }
                        }
                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                val primaryColorHex by settingsViewModel.primaryColor.collectAsState()
                FloatingActionButton(
                    onClick = {
                        isClicked = true
                        navController.navigate("add_task")
                    },
                    containerColor = primaryColorHex.fromHex(),
                    modifier = Modifier.scale(scale)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            ) {
                if (showProgress) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TaskProgressIndicator(viewModel)
                    }
                }

                if (tasks.isEmpty()){
                    TaskList(tasks)
                }
                else{
                    LazyColumn(
                        state = reorderState.listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(8.dp)
                            .reorderable(reorderState)
                    ) {
                        itemsIndexed(tasks, key = { _, task -> task.id }) { index, task ->
                            var dismissed by remember { mutableStateOf(false) }

                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut() + slideOutVertically()
                            ) {
                                // Wrap each item with ReorderableItem and add long press detection.
                                ReorderableItem(state = reorderState, key = task.id) { isDragging ->
                                    // Optionally, animate placement if dragging.
                                    Box(
                                        modifier = Modifier
                                            .animateItemPlacement()
                                            .detectReorderAfterLongPress(reorderState)
                                            .pointerInput(Unit) {
                                                detectHorizontalDragGestures { _, dragAmount ->
                                                    coroutineScope.launch {
                                                        if (dragAmount > 100) {
                                                            dismissed = true

                                                            if (task.status == "Completed") {
                                                                snackbarHostState.showSnackbar(
                                                                    "Already Completed",
                                                                    duration = SnackbarDuration.Short
                                                                )
                                                                return@launch
                                                            }


                                                            val result = snackbarHostState.showSnackbar(
                                                                "Task Completed", actionLabel = "Undo",
                                                                duration = SnackbarDuration.Short
                                                            )
                                                            if (result != SnackbarResult.ActionPerformed) {
                                                                viewModel.markTaskCompleted(task)
                                                            } else
                                                                dismissed = false
                                                        } else if (dragAmount < -100) {
                                                            dismissed = true
                                                            val result = snackbarHostState.showSnackbar(
                                                                "Task Deleted", actionLabel = "Undo",
                                                                duration = SnackbarDuration.Short
                                                            )
                                                            if (result != SnackbarResult.ActionPerformed) {
                                                                viewModel.deleteTask(task)
                                                            } else {
                                                                dismissed = false
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                    ) {
                                        TaskItem(
                                            task = task,
                                            onClick = { navController.navigate("task_detail/${task.id}") },
                                            onRemove = { viewModel.deleteTask(task) },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyState() {
    EmptyState()
}

@Composable
fun TaskList(tasks: List<Task>) {
    if (tasks.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn {
            items(tasks) { task ->  // ✅ Passing List<Task> correctly
                TaskItem(task, onClick = { /* Handle click */ }, onRemove = { /* Handle remove */ })

            }
        }

    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Priority: ${task.priority}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Due Date: ${task.dueDate}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(5.dp))
            AnimatedContent(
                targetState = task.status,
                transitionSpec = { fadeIn(tween(300)) with fadeOut(tween(300)) }
            ) { status ->
                Text(
                    text = if (status == "Completed") "✅ Completed" else "⏳ Pending",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (status == "Completed") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = onRemove) {
                Text("Remove Task")
            }
        }
    }
}

@Composable
fun TaskProgressIndicator(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val completedTasks = tasks.count { it.status == "Completed" }
    val progress = if (tasks.isNotEmpty()) completedTasks.toFloat() / tasks.size else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    Column (modifier = Modifier.padding(8.dp)){
        Text(text = "Completed Tasks: $completedTasks", style = MaterialTheme.typography.bodyMedium)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        ) {
            CircularProgressIndicator(
                progress = animatedProgress,
                strokeWidth = 8.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No tasks yet! 🚀",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Stay productive and start by adding your first task!",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tap the + button to begin.",
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }

}
