package com.example.edumension.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.edumension.data.BossEnemy
import com.example.edumension.data.DifficultyTier
import com.example.edumension.ui.CatchResult
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.theme.*
import kotlinx.coroutines.delay

/** ค่า difficulty ของมินิเกมตาม tier */
private data class MinigameParams(
    val speed: Float,
    val zoneWidth: Float,
    val timeLimit: Int
)

private fun paramsFor(tier: DifficultyTier) = when (tier) {
    DifficultyTier.COMMON    -> MinigameParams(speed = 0.006f, zoneWidth = 0.45f, timeLimit = 7)
    DifficultyTier.RARE      -> MinigameParams(speed = 0.010f, zoneWidth = 0.35f, timeLimit = 6)
    DifficultyTier.EPIC      -> MinigameParams(speed = 0.014f, zoneWidth = 0.25f, timeLimit = 5)
    DifficultyTier.LEGENDARY -> MinigameParams(speed = 0.020f, zoneWidth = 0.16f, timeLimit = 4)
    DifficultyTier.MYTHIC    -> MinigameParams(speed = 0.028f, zoneWidth = 0.10f, timeLimit = 3)
}

@Composable
fun CatchPhaseScreen(
    viewModel: GameViewModel,
    onDone: () -> Unit
) {
    val boss by viewModel.currentBoss.collectAsState()
    val catchRound by viewModel.catchRound.collectAsState()
    val catchResult by viewModel.catchResult.collectAsState()
    val totalRounds = viewModel.totalCatchRounds

    // ถ้า phase จบแล้ว (success/failed) → navigate ไป result
    LaunchedEffect(catchResult) {
        if (catchResult != CatchResult.NONE) {
            delay(2000)
            onDone()
        }
    }

    // Show final result overlay or minigame
    if (catchResult != CatchResult.NONE) {
        CatchFinalResult(boss = boss, success = catchResult == CatchResult.SUCCESS)
    } else {
        CatchRoundGame(
            boss = boss,
            currentRound = catchRound,
            totalRounds = totalRounds,
            onRoundSuccess = { viewModel.onCatchRoundSuccess() },
            onRoundFail = { viewModel.onCatchRoundFail() }
        )
    }
}

/** มินิเกมแต่ละรอบ */
@Composable
private fun CatchRoundGame(
    boss: BossEnemy,
    currentRound: Int,
    totalRounds: Int,
    onRoundSuccess: () -> Unit,
    onRoundFail: () -> Unit
) {
    val params = remember(boss.tier) { paramsFor(boss.tier) }

    // ── game state — key on currentRound to reset each round ──────────────
    var indicatorPos by remember(currentRound) { mutableFloatStateOf(0f) }
    var goingRight   by remember(currentRound) { mutableStateOf(true) }
    var timeLeft     by remember(currentRound) { mutableIntStateOf(params.timeLimit) }
    // null = กำลังเล่น, true = จับได้, false = จับไม่ได้
    var roundResult  by remember(currentRound) { mutableStateOf<Boolean?>(null) }

    // ── moving indicator loop ─────────────────────────────────────────────
    LaunchedEffect(currentRound, roundResult) {
        if (roundResult != null) return@LaunchedEffect
        while (true) {
            delay(16L)
            indicatorPos += if (goingRight) params.speed else -params.speed
            if (indicatorPos >= 1f) { indicatorPos = 1f; goingRight = false }
            if (indicatorPos <= 0f) { indicatorPos = 0f; goingRight = true }
        }
    }

    // ── countdown ─────────────────────────────────────────────────────────
    LaunchedEffect(currentRound, roundResult) {
        if (roundResult != null) return@LaunchedEffect
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        if (roundResult == null) roundResult = false
    }

    // ── auto-proceed after round result ───────────────────────────────────
    LaunchedEffect(roundResult) {
        if (roundResult == null) return@LaunchedEffect
        delay(1400L)
        if (roundResult == true) onRoundSuccess() else onRoundFail()
    }

    // ── catch zone bounds ─────────────────────────────────────────────────
    val zoneStart = (0.5f - params.zoneWidth / 2f).coerceAtLeast(0.02f)
    val zoneEnd   = (0.5f + params.zoneWidth / 2f).coerceAtMost(0.98f)

    fun checkCatch(): Boolean = indicatorPos in zoneStart..zoneEnd

    // ── Boss pulse animation ──────────────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "pulseScale"
    )

    // ── UI ────────────────────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(boss.colorStart).copy(alpha = 0.15f), Indigo50, Color.White)
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Round indicator ───────────────────────────────────────────────
        Text(
            "จับ ${boss.name}!",
            fontSize = 14.sp, fontWeight = FontWeight.Bold,
            color = Indigo400, letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Round progress dots
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(totalRounds) { i ->
                val dotColor = when {
                    i < currentRound -> Green500
                    i == currentRound && roundResult == true -> Green500
                    i == currentRound && roundResult == false -> Red500
                    i == currentRound -> Indigo500
                    else -> Slate200
                }
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(dotColor, CircleShape)
                )
            }
        }
        Text(
            "รอบที่ ${currentRound + 1} / $totalRounds",
            fontSize = 12.sp, color = Slate500, fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── Boss sprite ───────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(if (roundResult == null) pulseScale else 1f)
                .background(
                    Brush.radialGradient(listOf(Color(boss.colorStart), Color(boss.colorEnd))),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = boss.imageUrl,
                contentDescription = boss.name,
                modifier = Modifier.size(140.dp)
            )
            if (roundResult != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            if (roundResult == true) Color(0x8022C55E) else Color(0x80EF4444),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (roundResult == true) Icons.Filled.CheckCircle else Icons.Filled.Clear,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Name + tier badge ─────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(boss.name, fontSize = 24.sp, fontWeight = FontWeight.Black, color = Indigo900)
            Text(
                boss.tier.label,
                fontSize = 10.sp, fontWeight = FontWeight.Black,
                color = Color.White,
                modifier = Modifier
                    .background(Color(boss.tier.colorHex), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
        Text("${boss.type} Type", fontSize = 12.sp, color = Slate400, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(24.dp))

        // ── Round result message ──────────────────────────────────────────
        if (roundResult != null) {
            Text(
                if (roundResult == true) "✅ ผ่านรอบที่ ${currentRound + 1}!"
                else "❌ พลาด! ${boss.name} หนีไปแล้ว...",
                fontSize = 16.sp, fontWeight = FontWeight.Bold,
                color = if (roundResult == true) Green700 else Red600,
                textAlign = TextAlign.Center
            )
        } else {
            // ── Instruction + countdown ───────────────────────────────
            Text(
                "กด THROW! เมื่อตัวชี้อยู่ในโซนสีเขียว",
                fontSize = 13.sp, color = Slate500, textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "⏱ $timeLeft",
                fontSize = 22.sp, fontWeight = FontWeight.Black,
                color = if (timeLeft <= 2) Red500 else Indigo600
            )
            Spacer(modifier = Modifier.height(16.dp))

            // ── Timing bar ────────────────────────────────────────────
            TimingBar(
                indicatorPos = indicatorPos,
                zoneStart = zoneStart,
                zoneEnd = zoneEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Throw button ──────────────────────────────────────────
            Button(
                onClick = { roundResult = checkCatch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Filled.CatchingPokemon, contentDescription = null, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("THROW!", fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            }
        }
    }
}

/** ผลลัพธ์สุดท้าย — จับสำเร็จ / Boss หนีไป */
@Composable
private fun CatchFinalResult(boss: BossEnemy, success: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    if (success) listOf(Color(0xFF10B981).copy(alpha = 0.1f), Color.White)
                    else listOf(Color(0xFFEF4444).copy(alpha = 0.1f), Color.White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        Brush.radialGradient(listOf(Color(boss.colorStart), Color(boss.colorEnd))),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = boss.imageUrl,
                    contentDescription = boss.name,
                    modifier = Modifier.size(160.dp)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            if (success) Color(0x4022C55E) else Color(0x40EF4444),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (success) Icons.Filled.CheckCircle else Icons.Filled.Clear,
                        contentDescription = null, tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                if (success) "🎉 จับ ${boss.name} สำเร็จ!" else "💨 ${boss.name} หนีไปแล้ว...",
                fontSize = 22.sp, fontWeight = FontWeight.Black,
                color = if (success) Green700 else Red600,
                textAlign = TextAlign.Center
            )
            if (success) {
                Text(
                    "${boss.name} เข้า Collection แล้ว!",
                    fontSize = 14.sp, color = Green500, fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 6.dp)
                )
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
