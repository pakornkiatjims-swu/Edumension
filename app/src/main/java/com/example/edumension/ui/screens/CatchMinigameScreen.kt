package com.example.edumension.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.edumension.data.CatchablePokemon
import com.example.edumension.data.DifficultyTier
import com.example.edumension.ui.theme.*
import kotlinx.coroutines.delay

/** ค่า difficulty ของมินิเกมตาม tier */
private data class MinigameParams(
    val speed: Float,        // ความเร็วของตัวชี้ (0–1 ต่อ frame ~16ms)
    val zoneWidth: Float,    // ความกว้าง catch zone (0–1 เทียบกับ bar ทั้งหมด)
    val timeLimit: Int       // วินาทีที่มีสิทธิ์กด
)

private fun paramsFor(tier: DifficultyTier) = when (tier) {
    DifficultyTier.EASY   -> MinigameParams(speed = 0.007f, zoneWidth = 0.40f, timeLimit = 6)
    DifficultyTier.MEDIUM -> MinigameParams(speed = 0.014f, zoneWidth = 0.25f, timeLimit = 5)
    DifficultyTier.HARD   -> MinigameParams(speed = 0.024f, zoneWidth = 0.12f, timeLimit = 4)
}

@Composable
fun CatchMinigameScreen(
    pokemon: CatchablePokemon,
    onSuccess: () -> Unit,
    onFail: () -> Unit
) {
    val params = remember { paramsFor(pokemon.tier) }

    // ── game state ────────────────────────────────────────────────────────────
    var indicatorPos by remember { mutableFloatStateOf(0f) }
    var goingRight   by remember { mutableStateOf(true) }
    var timeLeft     by remember { mutableIntStateOf(params.timeLimit) }
    // null = กำลังเล่น, true = จับได้, false = จับไม่ได้
    var result       by remember { mutableStateOf<Boolean?>(null) }

    // ── moving indicator loop ─────────────────────────────────────────────────
    LaunchedEffect(result) {
        if (result != null) return@LaunchedEffect
        while (true) {
            delay(16L)
            indicatorPos += if (goingRight) params.speed else -params.speed
            if (indicatorPos >= 1f) { indicatorPos = 1f; goingRight = false }
            if (indicatorPos <= 0f) { indicatorPos = 0f; goingRight = true }
        }
    }

    // ── countdown ─────────────────────────────────────────────────────────────
    LaunchedEffect(result) {
        if (result != null) return@LaunchedEffect
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        if (result == null) result = false   // timeout = fail
    }

    // ── auto-dismiss after result ─────────────────────────────────────────────
    LaunchedEffect(result) {
        if (result == null) return@LaunchedEffect
        delay(1600L)
        if (result == true) onSuccess() else onFail()
    }

    // ── catch zone bounds ─────────────────────────────────────────────────────
    val zoneStart = (0.5f - params.zoneWidth / 2f).coerceAtLeast(0.02f)
    val zoneEnd   = (0.5f + params.zoneWidth / 2f).coerceAtMost(0.98f)

    fun checkCatch(): Boolean = indicatorPos in zoneStart..zoneEnd

    // ── UI ────────────────────────────────────────────────────────────────────
    Dialog(
        onDismissRequest = { /* ไม่ให้ dismiss เอง */ },
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Header ────────────────────────────────────────────────────
                Text(
                    "Pokemon ปรากฏตัว!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Indigo400,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ── Pokemon sprite ────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(pokemon.colorStart), Color(pokemon.colorEnd))
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(120.dp)
                    )
                    // Result overlay
                    if (result != null) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    if (result == true) Color(0x8022C55E) else Color(0x80EF4444),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (result == true) Icons.Filled.CheckCircle else Icons.Filled.Clear,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── Name + tier badge ─────────────────────────────────────────
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        pokemon.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Indigo900
                    )
                    Text(
                        pokemon.tier.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(pokemon.tier.colorHex), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${pokemon.type} Type",
                    fontSize = 12.sp,
                    color = Slate400,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Result message ────────────────────────────────────────────
                if (result != null) {
                    Text(
                        if (result == true) "🎉 จับได้! ${pokemon.name} เข้า Collection แล้ว!"
                        else "💨 ${pokemon.name} หนีไปแล้ว...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (result == true) Green700 else Red600,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    // ── Instruction + countdown ───────────────────────────────
                    Text(
                        "กด THROW! เมื่อตัวชี้อยู่ในโซนสีเขียว",
                        fontSize = 13.sp,
                        color = Slate500,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "⏱ $timeLeft",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = if (timeLeft <= 2) Red500 else Indigo600
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // ── Timing bar ────────────────────────────────────────────
                    TimingBar(
                        indicatorPos  = indicatorPos,
                        zoneStart     = zoneStart,
                        zoneEnd       = zoneEnd,
                        modifier      = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Throw button ──────────────────────────────────────────
                    Button(
                        onClick = { result = checkCatch() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Icon(
                            Icons.Filled.CatchingPokemon,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "THROW!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimingBar(
    indicatorPos: Float,
    zoneStart: Float,
    zoneEnd: Float,
    modifier: Modifier = Modifier
) {
    val animPos by animateFloatAsState(
        targetValue = indicatorPos,
        animationSpec = tween(durationMillis = 16),
        label = "indicator"
    )

    BoxWithConstraints(modifier = modifier.background(Slate100)) {
        val totalWidth = maxWidth

        // Catch zone (green)
        Box(
            modifier = Modifier
                .offset(x = totalWidth * zoneStart)
                .width(totalWidth * (zoneEnd - zoneStart))
                .fillMaxHeight()
                .background(Green300.copy(alpha = 0.6f))
        )

        // Center tick
        Box(
            modifier = Modifier
                .offset(x = totalWidth * 0.5f - 1.dp)
                .width(2.dp)
                .fillMaxHeight()
                .background(Green700.copy(alpha = 0.4f))
        )

        // Moving indicator
        Box(
            modifier = Modifier
                .offset(x = (totalWidth * animPos) - 10.dp)
                .size(20.dp)
                .align(Alignment.CenterStart)
                .background(Indigo600, CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
    }
}
