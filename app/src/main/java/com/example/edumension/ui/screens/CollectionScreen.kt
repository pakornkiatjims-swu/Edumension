package com.example.edumension.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.edumension.data.Linguamon
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    viewModel: GameViewModel,
    onBackHome: () -> Unit,
    onStartTraining: () -> Unit
) {
    val stats by viewModel.playerStats.collectAsState()
    var selectedLinguamon by remember { mutableStateOf<Linguamon?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Show Bottom Sheet when something is selected
    if (selectedLinguamon != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedLinguamon = null },
            sheetState = sheetState,
            containerColor = Color.Transparent,
            dragHandle = null
        ) {
            LinguamonDetailSheet(
                linguamon = selectedLinguamon!!,
                onDismiss = { selectedLinguamon = null },
                onTrain = { linguamon ->
                    selectedLinguamon = null
                    viewModel.startTraining(linguamon)
                    onStartTraining()
                },
                onEvolve = { linguamon ->
                    viewModel.evolveLinguamon(linguamon.id)
                    selectedLinguamon = null
                },
                onRelease = { linguamon ->
                    viewModel.releaseLinguamon(linguamon.id)
                    selectedLinguamon = null
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
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
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    "คอลเลกชัน",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Indigo900
                )
                Text(
                    "${stats.linguamonCollected.size} ตัว",
                    fontSize = 13.sp,
                    color = Slate400,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // ── Empty State ──────────────────────────────────────────────────────
        if (stats.linguamonCollected.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("📦", fontSize = 64.sp)
                    Text(
                        "ยังไม่มี Linguamon",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500
                    )
                    Text(
                        "ไปเล่นเกมเพื่อจับ Linguamon ใหม่!",
                        fontSize = 14.sp,
                        color = Slate400
                    )
                    Button(
                        onClick = onBackHome,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                    ) {
                        Text("ไปเล่นเกม")
                    }
                }
            }
        } else {
            // ── List ─────────────────────────────────────────────────────────
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(stats.linguamonCollected, key = { it.id }) { linguamon ->
                    LinguamonListItem(
                        linguamon = linguamon,
                        isSelected = selectedLinguamon?.id == linguamon.id,
                        onClick = { selectedLinguamon = linguamon }
                    )
                }
            }
        }
    }
}

@Composable
fun LinguamonListItem(
    linguamon: Linguamon,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Indigo500 else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(2.dp, borderColor, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(linguamon.colorStart), Color(linguamon.colorEnd))
                        ),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (linguamon.imageUrl != null) {
                    AsyncImage(
                        model = linguamon.imageUrl,
                        contentDescription = linguamon.name,
                        modifier = Modifier.size(80.dp)
                    )
                } else {
                    Text(text = linguamon.icon, fontSize = 48.sp)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = linguamon.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    Text(
                        text = linguamon.type.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Slate500,
                        modifier = Modifier
                            .background(Slate100, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = linguamon.description,
                    fontSize = 12.sp,
                    color = Slate500,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Lv. ${linguamon.level}",
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate400
                    )
                    Text(
                        "${linguamon.xp % 1000} / 1000 XP",
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate400
                    )
                }

                LinearProgressIndicator(
                    progress = { (linguamon.xp % 1000) / 1000f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(linguamon.colorEnd),
                    trackColor = Slate100
                )
            }
        }
    }
}
