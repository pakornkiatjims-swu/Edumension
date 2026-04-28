package com.example.edumension.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
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
import coil.compose.AsyncImage
import com.example.edumension.data.BossEnemy
import com.example.edumension.data.DifficultyTier
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameFinished: (allCorrect: Boolean) -> Unit
) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val currentIndex    by viewModel.currentQuestionIndex.collectAsState()
    val score           by viewModel.currentScore.collectAsState()
    val boss            by viewModel.currentBoss.collectAsState()
    val bossHp          by viewModel.bossHp.collectAsState()
    val totalQuestions = viewModel.totalQuestions
    val bossMaxHp      = viewModel.bossMaxHp

    // null = ยังไม่ตอบ, true = ถูก, false = ผิด
    var feedback by remember { mutableStateOf<Boolean?>(null) }

    // reset feedback เมื่อข้อเปลี่ยน
    LaunchedEffect(currentIndex) { feedback = null }

    // auto-advance หลังตอบ (ทั้งถูกและผิด)
    LaunchedEffect(feedback) {
        if (feedback != null) {
            delay(1800)
            if (currentIndex < totalQuestions - 1) {
                viewModel.nextQuestion()
            } else {
                onGameFinished(viewModel.allCorrect)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
    ) {
        // ── Top Bar ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onGameFinished(false) },
                modifier = Modifier.size(40.dp).background(Indigo50, CircleShape)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Indigo600)
            }

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / totalQuestions },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = Indigo500,
                    trackColor = Indigo100
                )
                Text(
                    "คำที่ ${currentIndex + 1} / $totalQuestions",
                    fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = Indigo400, modifier = Modifier.padding(top = 3.dp)
                )
            }

            // Score badge
            Row(
                modifier = Modifier
                    .background(Amber100, RoundedCornerShape(16.dp))
                    .border(1.dp, Amber200, RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Amber500, modifier = Modifier.size(15.dp))
                Text(score.toString(), fontWeight = FontWeight.Bold, color = Amber700, fontSize = 14.sp)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Boss Card ───────────────────────────────────────────────────
            BossCard(boss = boss, bossHp = bossHp, bossMaxHp = bossMaxHp, isHurt = feedback == true)

            Spacer(modifier = Modifier.height(16.dp))

            // ── Word Card ────────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "จงเลือกความหมายที่ถูกต้อง",
                        fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = Indigo400, letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        currentQuestion.word,
                        fontSize = 34.sp, fontWeight = FontWeight.Black, color = Indigo900
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Answer Feedback Banner (ผิด) ─────────────────────────────────
            AnimatedVisibility(
                visible = feedback == false,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Red100, RoundedCornerShape(14.dp))
                        .border(1.5.dp, Red400, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.Clear, contentDescription = null, tint = Red500, modifier = Modifier.size(22.dp))
                    Column {
                        Text("ผิด! คำตอบที่ถูกต้องคือ", fontSize = 11.sp, color = Red600, fontWeight = FontWeight.Bold)
                        Text(
                            currentQuestion.translation,
                            fontSize = 18.sp, fontWeight = FontWeight.Black, color = Red700
                        )
                    }
                }
            }

            // ── Correct Feedback Banner ──────────────────────────────────────
            AnimatedVisibility(
                visible = feedback == true,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green100, RoundedCornerShape(14.dp))
                        .border(1.5.dp, Green300, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Green500, modifier = Modifier.size(22.dp))
                    Text("ถูกต้อง! +10 คะแนน", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Green700)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Options Grid ────────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                currentQuestion.options.chunked(2).forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowOptions.forEach { optionText ->
                            val globalIndex = currentQuestion.options.indexOf(optionText)
                            OptionButton(
                                text = optionText,
                                isCorrect = currentQuestion.correctIndex == globalIndex,
                                feedback = feedback,
                                onClick = {
                                    feedback = viewModel.submitAnswer(globalIndex)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }   // end inner Column
    }   // end outer Column (screen root)
}

@Composable
fun BossCard(boss: BossEnemy, bossHp: Int, bossMaxHp: Int, isHurt: Boolean) {
    val tierColor = Color(boss.tier.colorHex)
    val hpFraction = (bossHp.toFloat() / bossMaxHp).coerceIn(0f, 1f)
    val animatedHp by animateFloatAsState(targetValue = hpFraction, animationSpec = tween(600), label = "bossHp")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Boss sprite
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        Brush.radialGradient(listOf(Color(boss.colorStart), Color(boss.colorEnd))),
                        RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = boss.imageUrl,
                    contentDescription = boss.name,
                    modifier = Modifier.size(90.dp)
                )
                // hurt flash overlay
                if (isHurt) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Red.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(boss.name, fontSize = 20.sp, fontWeight = FontWeight.Black, color = Indigo900)
                    // Difficulty badge
                    Text(
                        boss.tier.label,
                        fontSize = 10.sp, fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier
                            .background(tierColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("HP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate400)
                    Text("$bossHp / $bossMaxHp", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                }
                Spacer(modifier = Modifier.height(4.dp))
                // HP bar
                val barColor = when {
                    animatedHp > 0.5f -> Green500
                    animatedHp > 0.25f -> Amber500
                    else -> Red500
                }
                LinearProgressIndicator(
                    progress = { animatedHp },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = barColor,
                    trackColor = Slate100
                )
            }
        }
    }
}

@Composable
fun OptionButton(
    text: String,
    isCorrect: Boolean,
    feedback: Boolean?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        feedback == true  && isCorrect -> Green500
        feedback == false && isCorrect -> Green100
        feedback == false && !isCorrect -> Red50
        else -> Color.White
    }
    val textColor = when {
        feedback == true  && isCorrect -> Color.White
        feedback == false && isCorrect -> Green700
        else -> Indigo800
    }
    val borderColor = when {
        feedback == true  && isCorrect -> Green700
        feedback == false && isCorrect -> Green300
        else -> Indigo200
    }

    Button(
        onClick = onClick,
        enabled = feedback == null,
        modifier = modifier.height(68.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = textColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (feedback == null) 4.dp else 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            if (feedback == null) 0.dp else 2.dp, borderColor
        )
    ) {
        Text(text = text, fontSize = 17.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}
