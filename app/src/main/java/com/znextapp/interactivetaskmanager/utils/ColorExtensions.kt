package com.znextapp.interactivetaskmanager.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.Locale
import android.graphics.Color as AndroidColor


// ✅ Convert Color to Hex String (#RRGGBB)
fun Color.toHex(): String {
    return String.format(
        Locale.getDefault(),
        "#%06X",
        (0xFFFFFF and this.toArgb())  // Extract only RGB
    )
}

fun String.fromHex(): Color {
    return try {
        val parsedColor = AndroidColor.parseColor(this) // Convert HEX to Int color
        Color(parsedColor) // Convert Int to Jetpack Compose Color
    } catch (e: Exception) {
        Color(0xFF6200EE)  // Default color if parsing fails
    }
}