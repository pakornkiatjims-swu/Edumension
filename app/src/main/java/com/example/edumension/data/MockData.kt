package com.example.edumension.data

object MockData {
    val questions = listOf(
        Question(1, "Apple", "แอปเปิ้ล", listOf("กล้วย", "ส้ม", "แอปเปิ้ล", "องุ่น"), 2),
        Question(2, "Cat", "แมว", listOf("สุนัข", "แมว", "นก", "ปลา"), 1),
        Question(3, "House", "บ้าน", listOf("รถ", "บ้าน", "โรงเรียน", "โรงพยาบาล"), 1),
        Question(4, "Sun", "พระอาทิตย์", listOf("พระจันทร์", "ดวงดาว", "ท้องฟ้า", "พระอาทิตย์"), 3),
        Question(5, "Book", "หนังสือ", listOf("สมุด", "ดินสอ", "หนังสือ", "ปากกา"), 2),
        Question(6, "Water", "น้ำ", listOf("ไฟ", "ดิน", "ลม", "น้ำ"), 3),
        Question(7, "Friend", "เพื่อน", listOf("เพื่อน", "ศัตรู", "ครอบครัว", "คนแปลกหน้า"), 0),
        Question(8, "Happy", "มีความสุข", listOf("เศร้า", "โกรธ", "มีความสุข", "กลัว"), 2),
        Question(9, "Run", "วิ่ง", listOf("เดิน", "กระโดด", "คลาน", "วิ่ง"), 3),
        Question(10, "Beautiful", "สวยงาม", listOf("น่าเกลียด", "สวยงาม", "ธรรมดา", "แปลก"), 1)
    )

    val linguamons = listOf(
        Linguamon(1, "Flamey", "url_flamey", 5, 250, "A fiery friend that loves warm hugs."),
        Linguamon(2, "Aquafly", "url_aquafly", 3, 120, "Swims through the air as if it were water."),
        Linguamon(3, "Leafy", "url_leafy", 7, 450, "Grows a little more every time you learn a new word.")
    )

    val initialPlayerStats = PlayerStats(
        name = "Player 1",
        level = 3,
        totalXP = 820,
        totalScore = 1500,
        linguamonCollected = linguamons
    )
}
