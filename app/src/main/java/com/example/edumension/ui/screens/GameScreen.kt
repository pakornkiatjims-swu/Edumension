package com.example.edumension.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.components.LinguamonMascot
import com.example.edumension.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameFinished: () -> Unit
) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val score by viewModel.currentScore.collectAsState()
    val totalQuestions = viewModel.totalQuestions

    var feedback by remember { mutableStateOf<Boolean?>(null) } // true=correct, false=wrong, null=none

    // Reset feedback when question changes
    LaunchedEffect(currentIndex) {
        feedback = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
    ) {
        // Top Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onGameFinished() }, // Actually should be back home, but game logic needs handling. We'll simulate back home by navigating to result or popping
                modifier = Modifier
                    .size(40.dp)
                    .background(Indigo50, CircleShape)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Indigo600)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / totalQuestions },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = Indigo500,
                    trackColor = Indigo100
                )
                Text(
                    text = "คำที่ ${currentIndex + 1} จาก $totalQuestions",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Indigo400,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .background(Amber100, RoundedCornerShape(16.dp))
                    .border(1.dp, Amber200, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Filled.Star, contentDescription = "Score", tint = Amber500, modifier = Modifier.size(16.dp))
                Text(text = score.toString(), fontWeight = FontWeight.Bold, color = Amber700)
            }
        }

        // Main Game Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mascot with overlay
            Box(modifier = Modifier.padding(bottom = 24.dp)) {
                LinguamonMascot(
                    colorStart = 0xFFF59E0B, // Use Flamee colors or active monster
                    colorEnd = 0xFFEF4444,
                    isHappy = feedback == true,
                    isShaking = feedback == false
                )
                
                if (feedback == true) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 16.dp, y = (-16).dp)
                            .background(Green500, CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = "Correct", tint = Color.White)
                    }
                } else if (feedback == false) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 16.dp, y = (-16).dp)
                            .background(Red500, CircleShape)
                            .padding(8.dp)
                    ) {
                        Icon(Icons.Filled.Clear, contentDescription = "Wrong", tint = Color.White)
                    }
                }
            }

            // Word Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "จงเลือกความหมายที่ถูกต้อง",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Indigo400,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = currentQuestion.word,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = Indigo900
                    )
                }
            }

            // Options Grid
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OptionButton(
                        text = currentQuestion.options[0],
                        isCorrect = currentQuestion.correctIndex == 0,
                        feedback = feedback,
                        onClick = { 
                            feedback = viewModel.submitAnswer(0)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    OptionButton(
                        text = currentQuestion.options[1],
                        isCorrect = currentQuestion.correctIndex == 1,
                        feedback = feedback,
                        onClick = { 
                            feedback = viewModel.submitAnswer(1)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OptionButton(
                        text = currentQuestion.options[2],
                        isCorrect = currentQuestion.correctIndex == 2,
                        feedback = feedback,
                        onClick = { 
                            feedback = viewModel.submitAnswer(2)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    OptionButton(
                        text = currentQuestion.options[3],
                        isCorrect = currentQuestion.correctIndex == 3,
                        feedback = feedback,
                        onClick = { 
                            feedback = viewModel.submitAnswer(3)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Skip or Next Action
            LaunchedEffect(feedback) {
                if (feedback == true) {
                    delay(1500)
                    if (currentIndex < totalQuestions - 1) {
                        viewModel.nextQuestion()
                    } else {
                        onGameFinished()
                    }
                } else if (feedback == false) {
                    delay(1500)
                    feedback = null
                }
            }

            TextButton(
                onClick = {
                    if (currentIndex < totalQuestions - 1) {
                        viewModel.skipQuestion()
                    } else {
                        onGameFinished()
                    }
                },
                enabled = feedback == null
            ) {
                Text("ข้ามคำนี้", color = Indigo500, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Filled.FastForward, contentDescription = "Skip", tint = Indigo500, modifier = Modifier.size(18.dp))
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
        feedback == true && isCorrect -> Green500
        feedback == false && isCorrect -> Green100
        else -> Color.White
    }
    
    val textColor = when {
        feedback == true && isCorrect -> Color.White
        feedback == false && isCorrect -> Green700
        else -> Indigo800
    }

    val borderColor = when {
        feedback == true && isCorrect -> Green700
        feedback == false && isCorrect -> Green300
        else -> Indigo200
    }

    Button(
        onClick = onClick,
        enabled = feedback == null,
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = textColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = if (feedback == null) 4.dp else 0.dp),
        border = androidx.compose.foundation.BorderStroke(if (feedback == null) 0.dp else 2.dp, borderColor) // Simulate bottom border by adding full border when colored
    ) {
        Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
