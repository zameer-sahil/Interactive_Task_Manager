package com.znextapp.interactivetaskmanager.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import com.znextapp.interactivetaskmanager.utils.fromHex
import com.znextapp.interactivetaskmanager.viewmodel.SettingsViewModel

private val LightColorScheme = lightColorScheme(
    background = White,
    surface = LightGray,
    onPrimary = White,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black
)

private val DarkColorScheme = darkColorScheme(
    background = Black,
    surface = DarkGray,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White
)

@Composable
fun InteractiveTaskManagerTheme(
    settingsViewModel: SettingsViewModel,
    useDynamicColors: Boolean = true,
    content: @Composable () -> Unit
) {
    val primaryColorHex by settingsViewModel.primaryColor.collectAsState()
    val primaryColor = primaryColorHex.fromHex()
    val isDarkMode = isSystemInDarkTheme()

    val colorScheme = when {
        useDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDarkMode) dynamicDarkColorScheme(LocalContext.current) else dynamicLightColorScheme(LocalContext.current)
        }
        isDarkMode -> DarkColorScheme.copy(primary = primaryColor)
        else -> LightColorScheme.copy(primary = primaryColor)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}





