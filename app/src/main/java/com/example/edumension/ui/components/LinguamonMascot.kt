package com.example.edumension.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun LinguamonMascot(
    colorStart: Long,
    colorEnd: Long,
    isHappy: Boolean = false,
    isShaking: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot")
    
    // Bounce Animation (Pulse when idle, bounce when happy)
    val bounce by infiniteTransition.animateFloat(
        initialValue = if (isHappy) -15f else 0f,
        targetValue = if (isHappy) 15f else 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isHappy) 300 else 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Shake Animation
    val shake by infiniteTransition.animateFloat(
        initialValue = if (isShaking) -10f else 0f,
        targetValue = if (isShaking) 10f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shake"
    )

    val scale = if (isHappy) 1.1f else 1.0f

    Box(
        modifier = modifier
            .size(160.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // Shadow
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 8.dp)
                .width(96.dp)
                .height(16.dp)
                .blur(8.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.1f))
        )

        // Main Body
        Box(
            modifier = Modifier
                .size(128.dp)
                .graphicsLayer {
                    translationY = if (isShaking) 0f else bounce
                    translationX = if (isShaking) shake else 0f
                }
                .clip(CircleShape)
                .border(4.dp, Color.White, CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(colorStart), Color(colorEnd))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Eyes
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(if (isHappy) 16.dp else 12.dp, if (isHappy) 4.dp else 12.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Box(
                        modifier = Modifier
                            .size(if (isHappy) 16.dp else 12.dp, if (isHappy) 4.dp else 12.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
                // Mouth
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(if (isHappy) 12.dp else 4.dp)
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .background(Color.White)
                )
            }
        }
    }
}
