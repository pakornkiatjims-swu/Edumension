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

@Composable
fun CollectionScreen(
    viewModel: GameViewModel,
    onBackHome: () -> Unit
) {
    val stats by viewModel.playerStats.collectAsState()
    var selectedLinguamon by remember { mutableStateOf<Linguamon?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
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
                text = "คอลเลกชัน",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Indigo900,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // List
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(stats.linguamonCollected) { linguamon ->
                LinguamonListItem(
                    linguamon = linguamon,
                    isSelected = selectedLinguamon?.id == linguamon.id,
                    onClick = { selectedLinguamon = linguamon }
                )
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
                    Text("Lv. ${linguamon.level}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate400)
                    Text("${linguamon.xp} XP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate400)
                }

                LinearProgressIndicator(
                    progress = { (linguamon.xp % 1000).toFloat() / 1000f },
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
