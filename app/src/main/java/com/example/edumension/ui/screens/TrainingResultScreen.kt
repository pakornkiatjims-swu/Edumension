package com.example.edumension.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.theme.*

@Composable
fun TrainingResultScreen(
    viewModel: GameViewModel,
    onDone: () -> Unit
) {
    val correctCount by viewModel.correctCount.collectAsState()
    val trainTarget by viewModel.trainTarget.collectAsState()
    val totalQuestions = viewModel.totalQuestions
    val xpGained = correctCount * 50

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFFEEF2FF), Color.White))
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(48.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Training icon ───────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(Indigo50, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = Indigo600,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Text("ฝึกฝนเสร็จสิ้น!", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Indigo900)
                Text(
                    "ตอบถูก $correctCount / $totalQuestions ข้อ",
                    fontSize = 15.sp,
                    color = Slate500,
                    fontWeight = FontWeight.Medium
                )

                // ── Linguamon card ──────────────────────────────────────────
                if (trainTarget != null) {
                    val linguamon = trainTarget!!
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Indigo50)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(Color(linguamon.colorStart), Color(linguamon.colorEnd))
                                        ),
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (linguamon.imageUrl != null) {
                                    AsyncImage(
                                        model = linguamon.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.size(60.dp)
                                    )
                                } else {
                                    Text(linguamon.icon, fontSize = 36.sp)
                                }
                            }
                            Column {
                                Text(linguamon.name, fontSize = 18.sp, fontWeight = FontWeight.Black, color = Indigo900)
                                Text(
                                    if (xpGained > 0) "ได้รับ +$xpGained XP" else "ไม่ได้รับ XP (ไม่มีข้อถูก)",
                                    fontSize = 14.sp,
                                    color = if (xpGained > 0) Green700 else Slate400,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Lv.${linguamon.level}",
                                    fontSize = 12.sp,
                                    color = Slate400
                                )
                            }
                        }
                    }
                }

                // ── Stats row ───────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Green50, RoundedCornerShape(14.dp))
                            .border(1.dp, Green300, RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("XP ที่ได้รับ", fontSize = 11.sp, color = Green700, fontWeight = FontWeight.Bold)
                        Text("+$xpGained", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Green700)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Indigo50, RoundedCornerShape(14.dp))
                            .border(1.dp, Indigo100, RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("คำถูก", fontSize = 11.sp, color = Indigo600, fontWeight = FontWeight.Bold)
                        Text("$correctCount/$totalQuestions", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Indigo700)
                    }
                }

                // ── Done button ─────────────────────────────────────────────
                Button(
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text("กลับหน้าหลัก", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
