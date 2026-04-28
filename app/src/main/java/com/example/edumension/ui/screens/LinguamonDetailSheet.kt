package com.example.edumension.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.edumension.data.DifficultyTier
import com.example.edumension.data.Linguamon
import com.example.edumension.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/** Type → Color mapping */
private fun typeColor(type: String): Color = when (type.lowercase()) {
    "fire"     -> Color(0xFFFF6B35)
    "water"    -> Color(0xFF4FC3F7)
    "electric" -> Color(0xFFFFD600)
    "grass"    -> Color(0xFF66BB6A)
    "psychic"  -> Color(0xFFFF80AB)
    "ghost"    -> Color(0xFF9575CD)
    "dragon"   -> Color(0xFF5C6BC0)
    "fighting" -> Color(0xFFFF7043)
    "rock"     -> Color(0xFF8D6E63)
    "normal"   -> Color(0xFF90A4AE)
    else       -> Indigo400
}

private fun rarityColor(tier: DifficultyTier) = Color(tier.colorHex)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinguamonDetailSheet(
    linguamon: Linguamon,
    onDismiss: () -> Unit,
    onTrain: (Linguamon) -> Unit,
    onEvolve: (Linguamon) -> Unit,
    onRelease: (Linguamon) -> Unit
) {
    var showReleaseDialog by remember { mutableStateOf(false) }
    var showEvolveLockedDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // ── Release Confirmation Dialog ─────────────────────────────────────────
    if (showReleaseDialog) {
        AlertDialog(
            onDismissRequest = { showReleaseDialog = false },
            title = { Text("ปล่อย ${linguamon.name}?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "ต้องการปล่อย ${linguamon.name} ออกจากคอลเลกชันจริงหรือ? การกระทำนี้ไม่สามารถย้อนกลับได้",
                    color = Slate500
                )
            },
            confirmButton = {
                Button(
                    onClick = { showReleaseDialog = false; onRelease(linguamon) },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) { Text("ใช่ ปล่อย") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showReleaseDialog = false }) { Text("ยกเลิก") }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // ── Evolve Locked Dialog ────────────────────────────────────────────────
    if (showEvolveLockedDialog) {
        AlertDialog(
            onDismissRequest = { showEvolveLockedDialog = false },
            title = { Text("ยังวิวัฒนาการไม่ได้", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "${linguamon.name} ต้องมี Level 16 ขึ้นไปถึงจะวิวัฒนาการได้\n(ปัจจุบัน Level ${linguamon.level})",
                    color = Slate500
                )
            },
            confirmButton = {
                Button(onClick = { showEvolveLockedDialog = false }) { Text("รับทราบ") }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // ── Bottom Sheet Content ────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(Color.White, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
    ) {
        // Drag handle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(Slate200, RoundedCornerShape(2.dp))
            )
        }

        // ── A. Header ──────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        linguamon.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = Indigo900
                    )
                    Text(
                        "#${linguamon.id.toString().padStart(3, '0')}",
                        fontSize = 14.sp,
                        color = Slate400,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Type badge
                    val tColor = typeColor(linguamon.type)
                    Text(
                        linguamon.type,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier
                            .background(tColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                    // Rarity badge — derive from level
                    val rarity = levelToRarity(linguamon.level)
                    Text(
                        rarity.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier
                            .background(rarityColor(rarity), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .background(Slate100, CircleShape)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = Slate500)
            }
        }

        HorizontalDivider(color = Slate100)

        // Scrollable body
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── B. Main Image ───────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(linguamon.colorStart), Color(linguamon.colorEnd))
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .border(
                        3.dp,
                        Color(levelToRarity(linguamon.level).colorHex),
                        RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (linguamon.imageUrl != null) {
                    AsyncImage(
                        model = linguamon.imageUrl,
                        contentDescription = linguamon.name,
                        modifier = Modifier.size(220.dp)
                    )
                } else {
                    Text(linguamon.icon, fontSize = 120.sp, textAlign = TextAlign.Center)
                }
            }

            // ── C. Basic Stats ──────────────────────────────────────────────
            SectionTitle("สถิติพื้นฐาน")
            val xpInLevel = linguamon.xp % 1000
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Level",
                    value = "${linguamon.level}",
                    modifier = Modifier.weight(1f),
                    valueColor = Indigo700
                )
                StatCard(
                    label = "XP",
                    value = "$xpInLevel / 1000",
                    modifier = Modifier.weight(1f),
                    valueColor = Amber700
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "จับได้",
                    value = "${linguamon.timeCaught} ครั้ง",
                    modifier = Modifier.weight(1f),
                    valueColor = Green700
                )
                StatCard(
                    label = "จับเมื่อ",
                    value = formatDate(linguamon.caughtAt),
                    modifier = Modifier.weight(1f),
                    valueColor = Slate500
                )
            }
            // XP progress bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("ความก้าวหน้า XP", fontSize = 12.sp, color = Slate400, fontWeight = FontWeight.Medium)
                    Text("Level ${linguamon.level + 1}", fontSize = 12.sp, color = Indigo500, fontWeight = FontWeight.Bold)
                }
                val xpAnim by animateFloatAsState(
                    targetValue = xpInLevel / 1000f,
                    animationSpec = tween(800),
                    label = "xpBar"
                )
                LinearProgressIndicator(
                    progress = { xpAnim },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = Color(linguamon.colorEnd),
                    trackColor = Slate100
                )
            }

            // ── D. Combat Stats ─────────────────────────────────────────────
            SectionTitle("Combat Stats")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Indigo50),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CombatStatRow("HP",         linguamon.baseHp,      Color(0xFFEF4444))
                    CombatStatRow("Attack",     linguamon.baseAttack,  Color(0xFFFF7043))
                    CombatStatRow("Defense",    linguamon.baseDefense, Color(0xFF42A5F5))
                    CombatStatRow("Speed",      linguamon.baseSpeed,   Color(0xFF66BB6A))
                    CombatStatRow("Sp. Attack", linguamon.baseSpAtk,   Color(0xFFAB47BC))
                    CombatStatRow("Sp. Defense",linguamon.baseSpDef,   Color(0xFF26C6DA))
                }
            }

            // ── E. Description ──────────────────────────────────────────────
            SectionTitle("เกี่ยวกับ ${linguamon.name}")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Slate50),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Text(
                    linguamon.description,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Slate500
                )
            }

            // ── G. Action Buttons ───────────────────────────────────────────
            SectionTitle("การกระทำ")

            // Train
            Button(
                onClick = { onTrain(linguamon) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Icon(Icons.Filled.FitnessCenter, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ฝึกฝน (+${50} XP/ข้อ)", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            // Evolve
            val canEvolve = linguamon.level >= 16 && !linguamon.isEvolved && linguamon.evolutionId != null
            val hasEvolution = linguamon.evolutionId != null && !linguamon.isEvolved
            Button(
                onClick = {
                    if (canEvolve) onEvolve(linguamon)
                    else showEvolveLockedDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canEvolve) Color(0xFF8B5CF6) else Slate200,
                    contentColor = if (canEvolve) Color.White else Slate400
                ),
                elevation = ButtonDefaults.buttonElevation(if (canEvolve) 6.dp else 0.dp)
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (linguamon.isEvolved) "วิวัฒนาการแล้ว"
                    else if (!hasEvolution) "ไม่มีวิวัฒนาการ"
                    else if (canEvolve) "วิวัฒนาการ"
                    else "ต้อง Level 16 ขึ้นไป",
                    fontSize = 15.sp, fontWeight = FontWeight.Bold
                )
            }

            // Release
            OutlinedButton(
                onClick = { showReleaseDialog = true },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Red500),
                border = androidx.compose.foundation.BorderStroke(2.dp, Red500.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Filled.HeartBroken, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ปล่อยจากทีม", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ── Helper Composables ────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String) {
    Text(text, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Indigo900, letterSpacing = 0.5.sp)
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier, valueColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Indigo50),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, fontSize = 11.sp, color = Slate400, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = valueColor)
        }
    }
}

@Composable
private fun CombatStatRow(label: String, value: Int, barColor: Color) {
    val animValue by animateFloatAsState(
        targetValue = value / 100f,
        animationSpec = tween(700),
        label = label
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Slate500,
            modifier = Modifier.width(80.dp)
        )
        LinearProgressIndicator(
            progress = { animValue },
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = barColor,
            trackColor = Slate100
        )
        Text(
            value.toString(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = barColor,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.End
        )
    }
}

// ── Utilities ─────────────────────────────────────────────────────────────────

private fun levelToRarity(level: Int) = when {
    level >= 80 -> DifficultyTier.MYTHIC
    level >= 50 -> DifficultyTier.LEGENDARY
    level >= 30 -> DifficultyTier.EPIC
    level >= 15 -> DifficultyTier.RARE
    else        -> DifficultyTier.COMMON
}

private fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("d MMM", Locale.getDefault())
    return sdf.format(Date(millis))
}
