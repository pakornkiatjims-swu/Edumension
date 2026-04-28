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

/** ระดับ Rarity ของ Boss — กำหนดจำนวนรอบมินิเกมที่ต้องผ่านติดต่อกัน */
enum class DifficultyTier(
    val label: String,
    val colorHex: Long,
    val catchRounds: Int,
    val questionsPerGame: Int
) {
    COMMON("Common",    0xFF10B981, 1, 5),
    RARE("Rare",        0xFF3B82F6, 2, 6),
    EPIC("Epic",        0xFF8B5CF6, 3, 7),
    LEGENDARY("Legendary", 0xFFF59E0B, 4, 8),
    MYTHIC("Mythic",    0xFFEF4444, 5, 10)
}

/** ข้อมูล Boss Pokemon ที่เราจะสู้ด้วย — จับได้เมื่อตอบถูกหมด + ผ่านมินิเกม */
data class BossEnemy(
    val pokemonId: Int,
    val name: String,
    val tier: DifficultyTier,
    val type: String,
    val colorStart: Long,
    val colorEnd: Long,
    val hp: Int
) {
    /** CDN URL — ไม่ต้อง API call, Coil จะ lazy-load เอง */
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$pokemonId.png"

    /** แปลงเป็น Linguamon เพื่อเก็บใน Collection */
    fun toLinguamon(): Linguamon = Linguamon(
        id = pokemonId,
        name = name,
        type = type,
        colorStart = colorStart,
        colorEnd = colorEnd,
        level = when (tier) {
            DifficultyTier.COMMON -> 5
            DifficultyTier.RARE -> 15
            DifficultyTier.EPIC -> 30
            DifficultyTier.LEGENDARY -> 50
            DifficultyTier.MYTHIC -> 80
        },
        xp = when (tier) {
            DifficultyTier.COMMON -> 100
            DifficultyTier.RARE -> 400
            DifficultyTier.EPIC -> 1000
            DifficultyTier.LEGENDARY -> 2500
            DifficultyTier.MYTHIC -> 5000
        },
        icon = "⭐",
        description = "A $type type Pokemon of ${tier.label} rarity.",
        imageUrl = imageUrl
    )
}
