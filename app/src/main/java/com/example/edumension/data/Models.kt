package com.example.edumension.data

data class Question(
    val id: Int,
    val english: String,
    val thai: String,
    val options: List<String>,
    val correctIndex: Int
)

data class Linguamon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val level: Int,
    val xp: Int,
    val description: String = "A curious little Linguamon."
)

data class PlayerStats(
    val name: String = "Player 1",
    val level: Int = 1,
    val totalXP: Int = 0,
    val totalScore: Int = 0,
    val linguamonCollected: List<Linguamon> = emptyList()
)
