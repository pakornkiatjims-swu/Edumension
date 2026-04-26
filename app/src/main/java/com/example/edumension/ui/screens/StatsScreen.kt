package com.example.edumension.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.theme.*

@Composable
fun StatsScreen(
    viewModel: GameViewModel,
    onBackHome: () -> Unit
) {
    val stats by viewModel.playerStats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackHome,
                modifier = Modifier
                    .size(40.dp)
                    .background(Indigo50, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Indigo600)
            }
            Text(
                text = "สถิติการเล่น",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Indigo900,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Player Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Indigo600, Purple700)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Person, contentDescription = "User", tint = Color.White, modifier = Modifier.size(32.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = stats.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                        .padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Filled.EmojiEvents, contentDescription = "Badge", tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Veteran Explorer", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("เลเวล ${stats.level}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${stats.totalXP % 1000} / 1000 XP", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        LinearProgressIndicator(
                            progress = { (stats.totalXP % 1000).toFloat() / 1000f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Amber400,
                            trackColor = Color.Black.copy(alpha = 0.2f)
                        )
                    }
                }
            }

            // Stats Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "คำศัพท์ที่เรียน",
                    value = stats.wordsLearned.toString(),
                    icon = Icons.Filled.MenuBook,
                    iconColor = Color(0xFF3B82F6), // Blue500
                    bgColor = Color(0xFFEFF6FF)    // Blue50
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "คะแนนรวม",
                    value = stats.totalScore.toString(),
                    icon = Icons.Filled.Star,
                    iconColor = Amber500,
                    bgColor = Amber50
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "ความต่อเนื่อง",
                    value = "5 วัน",
                    icon = Icons.Filled.LocalFireDepartment,
                    iconColor = Color(0xFFF97316), // Orange500
                    bgColor = Color(0xFFFFF7ED)    // Orange50
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "จำนวน LINGUAMON",
                    value = stats.linguamonCollected.size.toString(),
                    icon = Icons.Filled.EmojiEvents,
                    iconColor = Purple500,
                    bgColor = Color(0xFFFAF5FF)    // Purple50
                )
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    bgColor: Color
) {
    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(1.dp, Color.Black.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Icon(icon, contentDescription = label, tint = iconColor, modifier = Modifier.size(24.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Slate800,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Slate500,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
