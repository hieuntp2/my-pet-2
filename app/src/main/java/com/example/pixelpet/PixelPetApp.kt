package com.example.pixelpet

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PixelPetApp() {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color(0xFF0B0F1A),
            surface = Color(0xFF0B0F1A),
            primary = Color(0xFF00E5FF),
            secondary = Color(0xFF7A5CFF),
        ),
    ) {
        PixelPetScreen()
    }
}
