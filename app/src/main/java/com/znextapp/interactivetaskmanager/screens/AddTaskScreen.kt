package com.znextapp.interactivetaskmanager.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.znextapp.interactivetaskmanager.viewmodel.TaskViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.znextapp.interactivetaskmanager.model.Task
import com.znextapp.interactivetaskmanager.ui.theme.InteractiveTaskManagerTheme // Import theme
import com.znextapp.interactivetaskmanager.utils.fromHex
import com.znextapp.interactivetaskmanager.viewmodel.SettingsViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()

) {

    val context = LocalContext.current

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var priority by remember { mutableStateOf("Low") }
    var dueDate by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            dueDate = "$dayOfMonth/${month + 1}/$year" // Format the selected date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    val primaryColorHex by settingsViewModel.primaryColor.collectAsState()


    // Wrap UI with theme to apply Material You colors
    InteractiveTaskManagerTheme(settingsViewModel = settingsViewModel) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add Task", color = MaterialTheme.colorScheme.onPrimary) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = primaryColorHex.fromHex())
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(text = "Add a new task", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Priority Dropdown
                Box {
                    OutlinedTextField(
                        value = priority,
                        onValueChange = {},
                        label = { Text("Priority") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Priority Dropdown")
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("Low", "Medium", "High").forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    priority = level
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Due Date Picker
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = {},
                    label = { Text("Due Date") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Date")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (title.text.isNotEmpty() && description.text.isNotEmpty() && dueDate.isNotEmpty()) {
                            val newTask = Task(
                                id = UUID.randomUUID().toString().hashCode(),
                                title = title.text,
                                description = description.text,
                                priority = priority,
                                dueDate = dueDate, // Save due date
                                status = "Pending" // Default status
                            )
                            viewModel.addTask(newTask)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColorHex.fromHex())
                ) {
                    Text("Add Task", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    }
}
