package com.example.edumension.data

data class Question(
    val id: Int,
    val word: String,
    val translation: String,
    val options: List<String>,
    val answer: String
) {
    val english get() = word
    val thai get() = translation
    val correctIndex get() = options.indexOf(answer)
}

data class Linguamon(
    val id: Int,
    val name: String,
    val type: String,
    val colorStart: Long,
    val colorEnd: Long,
    val level: Int,
    val xp: Int,
    val icon: String,
    val description: String,
    val imageUrl: String? = null
)

data class PlayerStats(
    val name: String = "Player 1",
    val level: Int = 12,
    val totalXP: Int = 2540,
    val wordsLearned: Int = 128,
    val totalScore: Int = 15400,
    val linguamonCollected: List<Linguamon> = emptyList()
)

/** ระดับความยากของ Boss */
enum class DifficultyTier(val label: String, val colorHex: Long) {
    EASY("ง่าย", 0xFF10B981),
    MEDIUM("ปานกลาง", 0xFFF59E0B),
    HARD("ยาก", 0xFFEF4444)
}

/** ข้อมูล Boss Pokemon ที่เราจะสู้ด้วย */
data class BossEnemy(
    val pokemonId: Int,
    val name: String,
    val tier: DifficultyTier,
    val colorStart: Long,
    val colorEnd: Long,
    val hp: Int,
    var imageUrl: String? = null
)

/**
 * Pokemon ที่ผู้ใช้มีสิทธิ์จับได้จากการตอบถูก
 * imageUrl ดึงจาก PokeAPI CDN โดยตรง — ไม่ต้อง API call เพิ่ม
 */
data class CatchablePokemon(
    val id: Int,
    val name: String,
    val tier: DifficultyTier,
    val type: String,
    val colorStart: Long,
    val colorEnd: Long
) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/other/official-artwork/$id.png"

    /** แปลงเป็น Linguamon เพื่อเก็บใน Collection */
    fun toLinguamon(): Linguamon = Linguamon(
        id = id,
        name = name,
        type = type,
        colorStart = colorStart,
        colorEnd = colorEnd,
        level = when (tier) { DifficultyTier.EASY -> 5; DifficultyTier.MEDIUM -> 15; DifficultyTier.HARD -> 30 },
        xp = when (tier) { DifficultyTier.EASY -> 100; DifficultyTier.MEDIUM -> 400; DifficultyTier.HARD -> 1000 },
        icon = "⭐",
        description = "A $type type Pokemon of ${tier.label} rarity.",
        imageUrl = imageUrl
    )
}
