package com.example.edumension.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
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
fun ResultScreen(
    viewModel: GameViewModel,
    onPlayAgain: () -> Unit,
    onBackHome: () -> Unit
) {
    val score by viewModel.currentScore.collectAsState()
    val xp by viewModel.currentXP.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Indigo50, Color.White)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(48.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
        ) {
            Box {
                // Top Gradient Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Indigo500, Purple500, Pink500)
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Trophy Icon
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .background(Amber100, CircleShape)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = "Trophy", tint = Amber500, modifier = Modifier.size(64.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "ยอดเยี่ยมมาก!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Indigo900
                    )
                    
                    Text(
                        text = "คุณเรียนรู้คำศัพท์ใหม่ได้สำเร็จ",
                        fontSize = 16.sp,
                        color = Indigo500,
                        modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                    )

                    // Score and XP Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(Indigo50, RoundedCornerShape(16.dp))
                                .border(1.dp, Indigo100, RoundedCornerShape(16.dp))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("SCORE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Indigo400)
                            Text(score.toString(), fontSize = 24.sp, fontWeight = FontWeight.Black, color = Indigo900)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color(0xFFFAF5FF), RoundedCornerShape(16.dp)) // Purple50 equivalent
                                .border(1.dp, Color(0xFFF3E8FF), RoundedCornerShape(16.dp)) // Purple100
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("XP GAINED", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Purple400) // need Purple400 defined or just hardcode
                            Text("+$xp", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Purple900) // Purple900
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Buttons
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                    ) {
                        Text("เล่นอีกครั้ง", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onBackHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Indigo600),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Indigo100)
                    ) {
                        Text("กลับหน้าหลัก", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

val Purple400 = Color(0xFFC084FC)
val Purple900 = Color(0xFF581C87)
