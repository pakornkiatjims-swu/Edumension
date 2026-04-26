package com.example.edumension.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edumension.data.Linguamon
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.theme.GamePrimary
import com.example.edumension.ui.theme.GameSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    viewModel: GameViewModel,
    onBackHome: () -> Unit
) {
    val stats by viewModel.playerStats.collectAsState()
    var selectedLinguamon by remember { mutableStateOf<Linguamon?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Linguamon Gallery") },
                navigationIcon = {
                    IconButton(onClick = onBackHome) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(stats.linguamonCollected) { linguamon ->
                    LinguamonCard(
                        linguamon = linguamon,
                        onClick = { selectedLinguamon = linguamon }
                    )
                }
            }
        }

        // Details Dialog
        selectedLinguamon?.let { linguamon ->
            AlertDialog(
                onDismissRequest = { selectedLinguamon = null },
                confirmButton = {
                    TextButton(onClick = { selectedLinguamon = null }) {
                        Text("Close")
                    }
                },
                title = { Text(linguamon.name, fontWeight = FontWeight.Bold) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(GameSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("*-*", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Level: ${linguamon.level}", fontWeight = FontWeight.Medium)
                        Text("XP: ${linguamon.xp}", color = GamePrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(linguamon.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }
    }
}

@Composable
fun LinguamonCard(linguamon: Linguamon, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(GameSecondary),
                contentAlignment = Alignment.Center
            ) {
                Text("*-*", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = linguamon.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Lv. ${linguamon.level}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
