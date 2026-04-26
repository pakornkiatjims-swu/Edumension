package com.example.edumension.data

data class Question(
    val id: Int,
    val word: String,
    val translation: String,
    val options: List<String>,
    val answer: String
) {
    // Adapter for old code that expects english, thai, correctIndex
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
    val description: String
) {
    val imageUrl get() = ""
}

data class PlayerStats(
    val name: String = "Player 1",
    val level: Int = 12,
    val totalXP: Int = 2540,
    val wordsLearned: Int = 128,
    val totalScore: Int = 15400,
    val linguamonCollected: List<Linguamon> = emptyList()
)
