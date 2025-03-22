//package com.znextapp.interactivetaskmanager.ui.screens
//
//import androidx.compose.animation.*
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.*
//import androidx.compose.foundation.gestures.detectHorizontalDragGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material.icons.rounded.FilterList
//import androidx.compose.material.icons.rounded.Sort
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.znextapp.interactivetaskmanager.enums.FilterOption
//import com.znextapp.interactivetaskmanager.enums.SortOption
//import com.znextapp.interactivetaskmanager.model.Task
//import com.znextapp.interactivetaskmanager.ui.theme.InteractiveTaskManagerTheme
//import com.znextapp.interactivetaskmanager.utils.fromHex
//import com.znextapp.interactivetaskmanager.viewmodel.SettingsViewModel
//import com.znextapp.interactivetaskmanager.viewmodel.TaskViewModel
//import kotlinx.coroutines.launch
//import org.burnoutcrew.reorderable.*
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
//@Composable
//fun TaskListScreen(navController: NavController, viewModel: TaskViewModel = hiltViewModel()) {
//    val tasks by viewModel.tasks.collectAsState()
//    val settingsViewModel: SettingsViewModel = hiltViewModel()
//    var showSortMenu by remember { mutableStateOf(false) }
//    var showFilterMenu by remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    val reorderState = rememberReorderableLazyListState(onMove = { from, to ->
//        coroutineScope.launch { viewModel.moveTask(from.index, to.index) }
//    })
//
//    InteractiveTaskManagerTheme(settingsViewModel = settingsViewModel) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Task Manager") },
//                    actions = {
//                        IconButton(onClick = { showSortMenu = true }) {
//                            Icon(Icons.Rounded.Sort, contentDescription = "Sort Tasks")
//                        }
//                        DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
//                            SortOption.entries.forEach { option ->
//                                DropdownMenuItem(
//                                    text = { Text(option.name.replace("_", " ")) },
//                                    onClick = {
//                                        viewModel.setSortOption(option)
//                                        showSortMenu = false
//                                    }
//                                )
//                            }
//                        }
//                        IconButton(onClick = { showFilterMenu = true }) {
//                            Icon(Icons.Rounded.FilterList, contentDescription = "Filter Tasks")
//                        }
//                        DropdownMenu(expanded = showFilterMenu, onDismissRequest = { showFilterMenu = false }) {
//                            FilterOption.entries.forEach { option ->
//                                DropdownMenuItem(
//                                    text = { Text(option.name.replace("_", " ")) },
//                                    onClick = {
//                                        viewModel.setFilterOption(option)
//                                        showFilterMenu = false
//                                    }
//                                )
//                            }
//                        }
//                        IconButton(onClick = { navController.navigate("settings") }) {
//                            Icon(Icons.Default.Settings, contentDescription = "Settings")
//                        }
//                    }
//                )
//            },
//            snackbarHost = { SnackbarHost(snackbarHostState) },
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = { navController.navigate("add_task") },
//                    containerColor = settingsViewModel.primaryColor.collectAsState().value.fromHex()
//                ) {
//                    Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
//                }
//            }
//        ) { paddingValues ->
//            LazyColumn(
//                state = reorderState.listState,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .padding(8.dp)
//                    .reorderable(reorderState)
//            ) {
//                itemsIndexed(tasks, key = { _, task -> task.id }) { index, task ->
//                    var dismissed by remember { mutableStateOf(false) }
//                    if (!dismissed) {
//                        ReorderableItem(state = reorderState, key = task.id) { isDragging ->
//                            Box(
//                                modifier = Modifier
//                                    .animateItemPlacement()
//                                    .pointerInput(Unit) {
//                                        detectHorizontalDragGestures { _, dragAmount ->
//                                            if (dragAmount > 100) {
//                                                coroutineScope.launch {
//                                                    viewModel.markTaskAsCompleted(task)
//                                                    snackbarHostState.showSnackbar("Task Completed", actionLabel = "Undo")
//                                                }
//                                            } else if (dragAmount < -100) {
//                                                coroutineScope.launch {
//                                                    dismissed = true
//                                                    val result = snackbarHostState.showSnackbar(
//                                                        "Task Deleted", actionLabel = "Undo"
//                                                    )
//                                                    if (result != SnackbarResult.ActionPerformed) {
//                                                        viewModel.deleteTask(task)
//                                                    } else {
//                                                        dismissed = false
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                            ) {
//                                TaskItem(
//                                    task = task,
//                                    onClick = { navController.navigate("task_detail/${task.id}") },
//                                    onRemove = { viewModel.deleteTask(task) },
//                                    modifier = Modifier.fillMaxWidth()
//                                )
//                            }
//                        }
//                        Divider()
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun TaskItem(
//    task: Task,
//    onClick: () -> Unit,
//    onRemove: () -> Unit,
//    onComplete: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var offsetX by remember { mutableStateOf(0f) }
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .pointerInput(Unit) {
//                detectHorizontalDragGestures { change, dragAmount ->
//                    change.consume()
//                    offsetX += dragAmount
//                    if (offsetX > 100) onComplete()
//                    if (offsetX < -100) onRemove()
//                }
//            },
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
//            Text(text = "Priority: ${task.priority}", style = MaterialTheme.typography.bodyMedium)
//            Text(text = "Due Date: ${task.dueDate}", style = MaterialTheme.typography.bodySmall)
//            Spacer(modifier = Modifier.height(5.dp))
//            AnimatedContent(
//                targetState = task.status,
//                transitionSpec = { fadeIn(tween(300)) with fadeOut(tween(300)) }
//            ) { status ->
//                Text(
//                    text = if (status == "Completed") "✅ Completed" else "⏳ Pending",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = if (status == "Completed") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
//                )
//            }
//        }
//    }
//}
