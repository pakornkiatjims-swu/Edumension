package com.example.edumension.data

object MockData {
    val questions = listOf(
        Question(1, "Hello", "สวัสดี", listOf("ลาก่อน", "ยินดีด้วย", "สวัสดี", "ขอบคุณ"), "สวัสดี"),
        Question(2, "Apple", "แอปเปิ้ล", listOf("แอปเปิ้ล", "กล้วย", "ส้ม", "องุ่น"), "แอปเปิ้ล"),
        Question(3, "Friend", "เพื่อน", listOf("ครอบครัว", "เพื่อน", "คุณครู", "นักเรียน"), "เพื่อน"),
        Question(4, "School", "โรงเรียน", listOf("บ้าน", "ตลาด", "โรงพยาบาล", "โรงเรียน"), "โรงเรียน"),
        Question(5, "Water", "น้ำ", listOf("น้ำ", "ข้าว", "ขนม", "ผลไม้"), "น้ำ")
    )

    val linguamons = listOf(
        Linguamon(1, "Flamee", "Fire", 0xFFFFA726, 0xFFF44336, 5, 450, "🔥", "A friendly fire spirit who loves warm words."),
        Linguamon(2, "Bubblo", "Water", 0xFF42A5F5, 0xFF00BCD4, 3, 210, "💧", "Always calm and flows with the rhythm of language."),
        Linguamon(3, "Leafy", "Plant", 0xFF66BB6A, 0xFF10B981, 8, 890, "🍃", "Grows stronger with every new word learned.")
    )

    val initialPlayerStats = PlayerStats(
        name = "Player 1",
        level = 12,
        totalXP = 2540,
        wordsLearned = 128,
        totalScore = 15400,
        linguamonCollected = linguamons
    )
}
