package com.example.edumension.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.theme.GamePrimary
import com.example.edumension.ui.theme.GameSecondary

@Composable
fun ResultScreen(
    viewModel: GameViewModel,
    onPlayAgain: () -> Unit,
    onBackHome: () -> Unit
) {
    val score by viewModel.currentScore.collectAsState()
    val xp by viewModel.currentXP.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Level Complete!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mascot
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(GameSecondary),
            contentAlignment = Alignment.Center
        ) {
            Text("\\(^o^)/", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Stats Card
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
                Text("Score", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("+$score", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = GamePrimary)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("XP Gained", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("+$xp XP", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = GameSecondary)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Action Buttons
        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GamePrimary)
        ) {
            Text("Play Again", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Back Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
