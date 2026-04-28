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
    val imageUrl: String? = null,
    // ── Combat Stats (0-100 scale) ──
    val baseHp: Int = 100,
    val baseAttack: Int = 50,
    val baseDefense: Int = 50,
    val baseSpeed: Int = 50,
    val baseSpAtk: Int = 50,
    val baseSpDef: Int = 50,
    // ── Meta ──
    val timeCaught: Int = 1,
    val isEvolved: Boolean = false,
    val evolutionId: Int? = null,
    val caughtAt: Long = System.currentTimeMillis()
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
    fun toLinguamon(): Linguamon {
        val (atk, def, spd, spAtk, spDef, baseHp) = when (tier) {
            DifficultyTier.COMMON    -> CombatStats(45, 40, 55, 40, 40, 70)
            DifficultyTier.RARE      -> CombatStats(60, 55, 65, 55, 55, 80)
            DifficultyTier.EPIC      -> CombatStats(75, 70, 75, 70, 70, 90)
            DifficultyTier.LEGENDARY -> CombatStats(90, 85, 85, 90, 85, 100)
            DifficultyTier.MYTHIC    -> CombatStats(100, 95, 100, 100, 95, 100)
        }
        return Linguamon(
            id = pokemonId,
            name = name,
            type = type,
            colorStart = colorStart,
            colorEnd = colorEnd,
            level = when (tier) {
                DifficultyTier.COMMON    -> 5
                DifficultyTier.RARE      -> 15
                DifficultyTier.EPIC      -> 30
                DifficultyTier.LEGENDARY -> 50
                DifficultyTier.MYTHIC    -> 80
            },
            xp = when (tier) {
                DifficultyTier.COMMON    -> 100
                DifficultyTier.RARE      -> 400
                DifficultyTier.EPIC      -> 1000
                DifficultyTier.LEGENDARY -> 2500
                DifficultyTier.MYTHIC    -> 5000
            },
            icon = "⭐",
            description = "A $type type Pokemon of ${tier.label} rarity.",
            imageUrl = imageUrl,
            baseHp = baseHp,
            baseAttack = atk,
            baseDefense = def,
            baseSpeed = spd,
            baseSpAtk = spAtk,
            baseSpDef = spDef,
            timeCaught = 1
        )
    }
}

/** Helper data class สำหรับ destructure stats */
private data class CombatStats(
    val atk: Int, val def: Int, val spd: Int,
    val spAtk: Int, val spDef: Int, val hp: Int
)
