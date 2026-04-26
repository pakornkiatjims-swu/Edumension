package com.example.edumension.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edumension.ui.components.LinguamonMascot
import com.example.edumension.ui.theme.*

@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onViewCollection: () -> Unit,
    onViewStats: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LinguamonMascot(
            colorStart = 0xFFA855F7, // Purple500
            colorEnd = 0xFF4F46E5,   // Indigo600
            isHappy = true,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "LINGUAMON",
            fontSize = 48.sp,
            fontWeight = FontWeight.Black,
            color = Indigo900,
            letterSpacing = (-1).sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "เก่งภาษาไปพร้อมกับคู่หูสุดน่ารัก!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Indigo600,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Start Game Button
            Button(
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Play", modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("เริ่มเล่น", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            // Row for Collection & Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onViewCollection,
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Amber400,
                        contentColor = Amber900
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text("คอลเลกชัน", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onViewStats,
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo100,
                        contentColor = Indigo800
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text("สถิติ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
