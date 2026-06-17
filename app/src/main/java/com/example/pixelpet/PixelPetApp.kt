package com.example.pixelpet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pixelpet.avatar.PetAnimationState

@Composable
fun PixelPetApp(
    debugVisualState: PetAnimationState? = null,
    onPetInteraction: () -> Unit = {},
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color(0xFF0B0F1A),
            surface = Color(0xFF0B0F1A),
            primary = Color(0xFF00E5FF),
            secondary = Color(0xFF7A5CFF),
        ),
    ) {
        val configuration = LocalConfiguration.current
        if (
            PhoneOrientationPolicy.shouldShowRotateBlocker(
                screenWidthDp = configuration.screenWidthDp,
                screenHeightDp = configuration.screenHeightDp,
            )
        ) {
            RotatePhoneBlocker()
        } else {
            PixelPetScreen(
                debugVisualState = debugVisualState,
                onPetInteraction = onPetInteraction,
            )
        }
    }
}

@Composable
private fun RotatePhoneBlocker() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Rotate your phone to landscape.",
            color = Color(0xFFD9FCFF),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp,
        )
    }
}
