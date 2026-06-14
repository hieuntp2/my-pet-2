package com.example.pixelpet.avatar

import android.provider.Settings
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.isActive

@Composable
fun PixelPetAvatar(
    modifier: Modifier = Modifier,
    showDebugInfo: Boolean = false,
) {
    val context = LocalContext.current
    val assets = remember(context) { PixelPetAssetLoader.load(context.assets) }
    val controller = remember(assets) {
        PetBehaviorController(
            idleAnimation = SpriteAnimation.looping(
                frameCount = assets.idle?.sheet?.frameCount ?: 4,
                frameDurationMs = 150L,
            ),
            blinkAnimation = SpriteAnimation.oneShot(
                frameDurationsMs = blinkDurationsFor(assets.blink?.sheet?.frameCount ?: 5),
            ),
        )
    }
    var frame by remember(controller) { mutableStateOf(controller.tick(nowMs = 0L)) }
    var idleMotion by remember(controller) { mutableStateOf(IdleMotionFrame.Neutral) }
    val motionEnabled = rememberSystemMotionEnabled()

    LaunchedEffect(controller, motionEnabled) {
        val startNanos = withFrameNanos { it }
        while (isActive) {
            withFrameNanos { frameTimeNanos ->
                val elapsedMs = (frameTimeNanos - startNanos) / 1_000_000L
                frame = controller.tick(elapsedMs)
                idleMotion = IdleMotion.frameAt(
                    elapsedMs = elapsedMs,
                    motionEnabled = motionEnabled,
                )
            }
        }
    }

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = idleMotion.translationY
                    scaleY = idleMotion.scaleY
                },
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val sheet = when (frame.state) {
                    PetAnimationState.LookingIdle -> assets.idle
                    PetAnimationState.LookingBlink -> assets.blink
                }

                if (assets.hasRequiredSheets && sheet != null) {
                    with(PixelPetRenderer) {
                        drawSprite(
                            spriteSheet = sheet,
                            frameIndex = frame.frameIndex,
                            lookOffsetX = frame.lookOffsetX,
                            lookOffsetY = frame.lookOffsetY,
                        )
                    }
                } else {
                    with(PixelPetRenderer) {
                        drawFallback(frame)
                    }
                }
            }
        }

        if (assets.missingAssets.isNotEmpty()) {
            BasicText(
                text = "Missing assets: ${assets.missingAssets.joinToString()}",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                style = TextStyle(
                    color = Color(0xFF7AF7FF),
                    fontSize = 12.sp,
                ),
            )
        } else if (showDebugInfo) {
            BasicText(
                text = "${frame.state}  frame=${frame.frameIndex}  nextBlink=${frame.nextBlinkInMs}ms",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                style = TextStyle(
                    color = Color(0xFF7AF7FF),
                    fontSize = 12.sp,
                ),
            )
        }
    }
}

@Composable
private fun rememberSystemMotionEnabled(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        val animatorScale = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        )
        animatorScale != 0f
    }
}

private fun blinkDurationsFor(frameCount: Int): List<Long> {
    require(frameCount > 0) { "Blink animation must have at least one frame." }

    return when (frameCount) {
        1 -> listOf(80L)
        6 -> listOf(40L, 60L, 60L, 80L, 60L, 60L)
        else -> List(frameCount) { index ->
            if (index == frameCount / 2) 80L else 60L
        }
    }
}
