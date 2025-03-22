package com.znextapp.interactivetaskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.znextapp.interactivetaskmanager.ui.navigation.AppNavigation

import com.znextapp.interactivetaskmanager.ui.theme.InteractiveTaskManagerTheme
import com.znextapp.interactivetaskmanager.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InteractiveTaskManagerTheme(settingsViewModel) {  // ✅ Pass settingsViewModel here
                AppNavigation(settingsViewModel)
            }
        }
    }
}

