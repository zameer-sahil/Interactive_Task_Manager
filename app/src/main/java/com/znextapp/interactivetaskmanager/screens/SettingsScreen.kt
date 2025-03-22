package com.znextapp.interactivetaskmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.znextapp.interactivetaskmanager.utils.fromHex
import com.znextapp.interactivetaskmanager.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel(), navController: NavHostController) {
    val primaryColorHex by viewModel.primaryColor.collectAsState()
    var selectedColor by remember { mutableStateOf(Color.Black) }


    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Customize Primary Color", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(10.dp))

            val colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5), Color(0xFFFF0266), Color(0xFFFF5722))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(8.dp)) {
                colors.forEach { color ->
                    IconButton(onClick = { selectedColor = color }) {
                        Surface(shape = CircleShape, color = color, modifier = Modifier.size(40.dp)) {}
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.updatePrimaryColor(selectedColor) // ✅ Update theme color
                    navController.navigate("task_list") { popUpTo("task_list") { inclusive = true } } // Navigate back
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColorHex.fromHex())

            ) {
                Text("Apply Theme")
            }
        }
    }
}

