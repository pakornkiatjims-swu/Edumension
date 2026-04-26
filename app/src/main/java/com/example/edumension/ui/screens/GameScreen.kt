package com.example.edumension.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.theme.GameError
import com.example.edumension.ui.theme.GamePrimary
import com.example.edumension.ui.theme.GameSecondary
import com.example.edumension.ui.theme.GameSuccess

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameFinished: () -> Unit
) {
    val currentQuestion by viewModel.currentQuestion.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val totalQuestions = viewModel.totalQuestions

    var showFeedback by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress bar
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / totalQuestions },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = GamePrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            text = "${currentIndex + 1} / $totalQuestions",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mascot area (top)
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(if (showFeedback && isCorrect) GameSuccess else GameSecondary),
            contentAlignment = Alignment.Center
        ) {
            Text(if (showFeedback && isCorrect) "^_^" else "O_O", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentQuestion.english,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "แปลว่าอะไร?",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Options
        currentQuestion.options.forEachIndexed { index, option ->
            Button(
                onClick = {
                    if (!showFeedback) {
                        isCorrect = viewModel.submitAnswer(index)
                        showFeedback = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showFeedback) {
                        if (index == currentQuestion.correctIndex) GameSuccess
                        else if (!isCorrect) GameError
                        else MaterialTheme.colorScheme.surfaceVariant
                    } else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (showFeedback && (index == currentQuestion.correctIndex || !isCorrect)) 
                        MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                ),
                enabled = !showFeedback
            ) {
                Text(text = option, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Feedback and Next Button
        AnimatedVisibility(visible = showFeedback) {
            Button(
                onClick = {
                    showFeedback = false
                    if (currentIndex < totalQuestions - 1) {
                        viewModel.nextQuestion()
                    } else {
                        onGameFinished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GamePrimary)
            ) {
                Text(if (currentIndex < totalQuestions - 1) "Next Question" else "Finish Game", fontSize = 18.sp)
            }
        }

        if (!showFeedback) {
            TextButton(onClick = { 
                if (currentIndex < totalQuestions - 1) {
                    viewModel.skipQuestion()
                } else {
                    onGameFinished()
                }
             }) {
                Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
