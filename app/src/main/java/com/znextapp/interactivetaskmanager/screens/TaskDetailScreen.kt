import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.znextapp.interactivetaskmanager.viewmodel.TaskViewModel
import com.znextapp.interactivetaskmanager.model.Task
import com.znextapp.interactivetaskmanager.ui.theme.InteractiveTaskManagerTheme
import com.znextapp.interactivetaskmanager.utils.fromHex
import com.znextapp.interactivetaskmanager.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Int,
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    var task by remember { mutableStateOf<Task?>(null) }
    val primaryColorHex by settingsViewModel.primaryColor.collectAsState()

    LaunchedEffect(taskId) {
        viewModel.getTaskById(taskId).collectLatest { fetchedTask ->
            task = fetchedTask
        }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(task) {
        task?.let {
            title = it.title
            description = it.description
            isCompleted = it.status == "Completed"
        }
    }

    // Animation State
    var isRevealed by remember { mutableStateOf(false) }
    var removeClip by remember { mutableStateOf(false) }

    // Start animation after a short delay
    LaunchedEffect(Unit) {
        delay(20)
        isRevealed = true
    }

    // Animate circular reveal
    val revealRadius by animateFloatAsState(
        targetValue = if (isRevealed) 3000f else 0f, // Expand outward
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        finishedListener = { removeClip = true } // Remove clip after animation ends
    )

    InteractiveTaskManagerTheme(settingsViewModel = settingsViewModel) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Task Details", color = Color.White) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = primaryColorHex.fromHex())
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .then(if (!removeClip) Modifier.clip(CircleShape) else Modifier) // Remove clip after reveal
                    .size(revealRadius.dp) // Animate size
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Status", style = MaterialTheme.typography.titleMedium)
                    Switch(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                    Text(if (isCompleted) "Completed" else "Pending")

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val updatedTask = task?.copy(
                                title = title,
                                description = description,
                                status = if (isCompleted) "Completed" else "Pending"
                            )
                            if (updatedTask != null) {
                                viewModel.updateTask(updatedTask)
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColorHex.fromHex(),contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Task", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            task?.let { viewModel.deleteTask(it) }
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error,contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete Task", color = Color.White)
                    }
                }
            }
        }
    }
}
