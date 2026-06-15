package com.example.pixelpet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.pixelpet.avatar.PetAnimationState
import com.example.pixelpet.avatar.PixelPetAvatar

@Composable
fun PixelPetScreen(debugVisualState: PetAnimationState? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        CyberBackground(modifier = Modifier.fillMaxSize())
        PixelPetAvatar(
            modifier = Modifier.fillMaxSize(),
            debugVisualState = debugVisualState,
        )
    }
}

@Composable
private fun CyberBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawRect(Color(0xFF0B0F1A))

        val gridStep = 32f
        var x = 0f
        while (x <= size.width) {
            drawLine(
                color = Color(0x1812304A),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f,
            )
            x += gridStep
        }

        var y = 0f
        while (y <= size.height) {
            drawLine(
                color = Color(0x1812304A),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f,
            )
            y += gridStep
        }

        drawCircle(
            color = Color(0x3312304A),
            radius = minOf(size.width, size.height) * 0.32f,
            center = center,
            style = Stroke(width = 1f),
        )
        drawLine(
            color = Color(0x447A5CFF),
            start = Offset(size.width * 0.12f, size.height * 0.78f),
            end = Offset(size.width * 0.88f, size.height * 0.78f),
            strokeWidth = 2f,
        )
    }
}
