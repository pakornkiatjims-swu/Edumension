package com.example.edumension.data

object MockData {

    val questions = listOf(
        // ── COMMON: Greetings & Basic Words (id 1-15) ───────────────────
        Question(1,  "Hello",     "สวัสดี",     listOf("ลาก่อน","ยินดีด้วย","สวัสดี","ขอบคุณ"), "สวัสดี"),
        Question(2,  "Goodbye",   "ลาก่อน",     listOf("ลาก่อน","สวัสดี","ขอบคุณ","โปรด"), "ลาก่อน"),
        Question(3,  "Thank you", "ขอบคุณ",     listOf("ขอโทษ","ขอบคุณ","ยินดีต้อนรับ","ไม่เป็นไร"), "ขอบคุณ"),
        Question(4,  "Sorry",     "ขอโทษ",      listOf("ขอบคุณ","ยินดีด้วย","ขอโทษ","โปรด"), "ขอโทษ"),
        Question(5,  "Please",    "โปรด",       listOf("ขอบคุณ","โปรด","ขอโทษ","ลาก่อน"), "โปรด"),
        Question(6,  "Yes",       "ใช่",        listOf("ไม่","ใช่","บางที","เสมอ"), "ใช่"),
        Question(7,  "No",        "ไม่",        listOf("ใช่","บางที","ไม่","ยัง"), "ไม่"),
        Question(8,  "Good",      "ดี",         listOf("ดี","ไม่ดี","สวย","แย่"), "ดี"),
        Question(9,  "Bad",       "ไม่ดี",      listOf("ดี","ไม่ดี","เก่ง","สวย"), "ไม่ดี"),
        Question(10, "Morning",   "เช้า",       listOf("เช้า","เย็น","กลางคืน","บ่าย"), "เช้า"),
        Question(11, "Night",     "กลางคืน",    listOf("เช้า","บ่าย","กลางคืน","เที่ยง"), "กลางคืน"),
        Question(12, "Today",     "วันนี้",     listOf("เมื่อวาน","วันนี้","พรุ่งนี้","สัปดาห์"), "วันนี้"),
        Question(13, "Tomorrow",  "พรุ่งนี้",   listOf("เมื่อวาน","วันนี้","พรุ่งนี้","สัปดาห์"), "พรุ่งนี้"),
        Question(14, "Love",      "รัก",        listOf("เกลียด","ชอบ","รัก","กลัว"), "รัก"),
        Question(15, "Happy",     "มีความสุข",  listOf("เศร้า","โกรธ","มีความสุข","ตกใจ"), "มีความสุข"),

        // ── RARE: Family, Colors & Body (id 16-30) ──────────────────────
        Question(16, "Mother",    "แม่",        listOf("พ่อ","แม่","พี่","น้อง"), "แม่"),
        Question(17, "Father",    "พ่อ",        listOf("แม่","ลุง","พ่อ","ปู่"), "พ่อ"),
        Question(18, "Brother",   "พี่ชาย",     listOf("น้องสาว","พี่ชาย","ลูกชาย","ลูกสาว"), "พี่ชาย"),
        Question(19, "Sister",    "พี่สาว",     listOf("พี่ชาย","พี่สาว","แม่","ป้า"), "พี่สาว"),
        Question(20, "Friend",    "เพื่อน",     listOf("ครอบครัว","เพื่อน","คุณครู","นักเรียน"), "เพื่อน"),
        Question(21, "Red",       "สีแดง",      listOf("สีแดง","สีน้ำเงิน","สีเขียว","สีเหลือง"), "สีแดง"),
        Question(22, "Blue",      "สีน้ำเงิน",  listOf("สีม่วง","สีน้ำเงิน","สีฟ้า","สีเทา"), "สีน้ำเงิน"),
        Question(23, "Green",     "สีเขียว",    listOf("สีเหลือง","สีเขียว","สีส้ม","สีน้ำตาล"), "สีเขียว"),
        Question(24, "Yellow",    "สีเหลือง",   listOf("สีส้ม","สีเหลือง","สีครีม","สีขาว"), "สีเหลือง"),
        Question(25, "Black",     "สีดำ",       listOf("สีขาว","สีดำ","สีเทา","สีน้ำตาล"), "สีดำ"),
        Question(26, "White",     "สีขาว",      listOf("สีดำ","สีเทา","สีขาว","สีครีม"), "สีขาว"),
        Question(27, "Head",      "หัว",        listOf("หัว","มือ","เท้า","ตา"), "หัว"),
        Question(28, "Hand",      "มือ",        listOf("เท้า","มือ","แขน","ขา"), "มือ"),
        Question(29, "Eye",       "ตา",         listOf("หู","จมูก","ตา","ปาก"), "ตา"),
        Question(30, "Heart",     "หัวใจ",      listOf("สมอง","หัวใจ","ปอด","ตับ"), "หัวใจ"),

        // ── EPIC: Places, Food & Nature (id 31-50) ──────────────────────
        Question(31, "School",    "โรงเรียน",   listOf("บ้าน","ตลาด","โรงพยาบาล","โรงเรียน"), "โรงเรียน"),
        Question(32, "Hospital",  "โรงพยาบาล",  listOf("โรงเรียน","ตลาด","โรงพยาบาล","สวนสาธารณะ"), "โรงพยาบาล"),
        Question(33, "Market",    "ตลาด",       listOf("ห้างสรรพสินค้า","ตลาด","โรงเรียน","โบสถ์"), "ตลาด"),
        Question(34, "Park",      "สวนสาธารณะ", listOf("ตลาด","บ้าน","สวนสาธารณะ","ห้องสมุด"), "สวนสาธารณะ"),
        Question(35, "Library",   "ห้องสมุด",   listOf("ห้องสมุด","โรงภาพยนตร์","สระว่ายน้ำ","สนามบิน"), "ห้องสมุด"),
        Question(36, "Airport",   "สนามบิน",    listOf("ท่าเรือ","สถานีรถไฟ","สนามบิน","สนามกีฬา"), "สนามบิน"),
        Question(37, "Apple",     "แอปเปิ้ล",   listOf("แอปเปิ้ล","กล้วย","ส้ม","องุ่น"), "แอปเปิ้ล"),
        Question(38, "Banana",    "กล้วย",      listOf("แอปเปิ้ล","กล้วย","มะม่วง","สตรอว์เบอร์รี"), "กล้วย"),
        Question(39, "Mango",     "มะม่วง",     listOf("มะม่วง","มะละกอ","กล้วย","ลิ้นจี่"), "มะม่วง"),
        Question(40, "Water",     "น้ำ",        listOf("น้ำ","ข้าว","ขนม","ผลไม้"), "น้ำ"),
        Question(41, "Rice",      "ข้าว",       listOf("ขนมปัง","ข้าว","เส้นก๋วยเตี๋ยว","ข้าวโพด"), "ข้าว"),
        Question(42, "Bread",     "ขนมปัง",     listOf("ข้าว","ขนมปัง","เค้ก","คุกกี้"), "ขนมปัง"),
        Question(43, "Milk",      "นม",         listOf("น้ำ","น้ำผลไม้","นม","ชา"), "นม"),
        Question(44, "Sun",       "ดวงอาทิตย์", listOf("ดวงจันทร์","ดาว","ดวงอาทิตย์","เมฆ"), "ดวงอาทิตย์"),
        Question(45, "Moon",      "ดวงจันทร์",  listOf("ดวงอาทิตย์","ดวงจันทร์","ดาว","ท้องฟ้า"), "ดวงจันทร์"),
        Question(46, "Rain",      "ฝน",         listOf("หิมะ","ฝน","ลม","พายุ"), "ฝน"),
        Question(47, "Mountain",  "ภูเขา",      listOf("แม่น้ำ","ทะเล","ภูเขา","ป่า"), "ภูเขา"),
        Question(48, "River",     "แม่น้ำ",     listOf("ทะเล","แม่น้ำ","น้ำตก","ทะเลสาบ"), "แม่น้ำ"),
        Question(49, "Tree",      "ต้นไม้",     listOf("ดอกไม้","ต้นไม้","หญ้า","ใบไม้"), "ต้นไม้"),
        Question(50, "Flower",    "ดอกไม้",     listOf("ต้นไม้","ดอกไม้","ผลไม้","ใบไม้"), "ดอกไม้"),

        // ── LEGENDARY: Animals, Numbers & Adjectives (id 51-70) ─────────
        Question(51, "Dog",       "หมา",        listOf("แมว","หมา","กระต่าย","นก"), "หมา"),
        Question(52, "Cat",       "แมว",        listOf("หมา","แมว","หนู","กิ้งก่า"), "แมว"),
        Question(53, "Bird",      "นก",         listOf("นก","ปลา","งู","กบ"), "นก"),
        Question(54, "Elephant",  "ช้าง",       listOf("เสือ","ช้าง","สิงโต","ยีราฟ"), "ช้าง"),
        Question(55, "Tiger",     "เสือ",       listOf("ช้าง","เสือ","สิงโต","หมี"), "เสือ"),
        Question(56, "Horse",     "ม้า",        listOf("ม้า","วัว","หมู","แกะ"), "ม้า"),
        Question(57, "Fish",      "ปลา",        listOf("กุ้ง","ปลา","ปู","หอย"), "ปลา"),
        Question(58, "One",       "หนึ่ง",      listOf("สอง","สาม","หนึ่ง","สี่"), "หนึ่ง"),
        Question(59, "Two",       "สอง",        listOf("หนึ่ง","สอง","สาม","ห้า"), "สอง"),
        Question(60, "Ten",       "สิบ",        listOf("ยี่สิบ","สิบ","ห้า","ร้อย"), "สิบ"),
        Question(61, "Hundred",   "ร้อย",       listOf("พัน","สิบ","ร้อย","ล้าน"), "ร้อย"),
        Question(62, "Big",       "ใหญ่",       listOf("เล็ก","ใหญ่","สูง","ต่ำ"), "ใหญ่"),
        Question(63, "Small",     "เล็ก",       listOf("ใหญ่","เล็ก","กว้าง","แคบ"), "เล็ก"),
        Question(64, "Hot",       "ร้อน",       listOf("เย็น","อุ่น","ร้อน","หนาว"), "ร้อน"),
        Question(65, "Cold",      "เย็น",       listOf("ร้อน","เย็น","อุ่น","ชื้น"), "เย็น"),
        Question(66, "Fast",      "เร็ว",       listOf("ช้า","เร็ว","หนัก","เบา"), "เร็ว"),
        Question(67, "Slow",      "ช้า",        listOf("เร็ว","ช้า","ยาว","สั้น"), "ช้า"),
        Question(68, "Beautiful", "สวยงาม",     listOf("น่าเกลียด","สวยงาม","ธรรมดา","แปลก"), "สวยงาม"),
        Question(69, "Strong",    "แข็งแรง",    listOf("อ่อนแอ","แข็งแรง","เหนื่อย","ขี้เกียจ"), "แข็งแรง"),
        Question(70, "Brave",     "กล้าหาญ",    listOf("ขี้กลัว","กล้าหาญ","เงียบ","ดัง"), "กล้าหาญ"),

        // ── MYTHIC: Verbs & Advanced (id 71-95) ─────────────────────────
        Question(71, "Run",       "วิ่ง",       listOf("เดิน","วิ่ง","กระโดด","ว่ายน้ำ"), "วิ่ง"),
        Question(72, "Eat",       "กิน",        listOf("ดื่ม","กิน","นอน","ยิ้ม"), "กิน"),
        Question(73, "Sleep",     "นอนหลับ",    listOf("ตื่น","นั่ง","นอนหลับ","ยืน"), "นอนหลับ"),
        Question(74, "Read",      "อ่าน",       listOf("เขียน","อ่าน","ฟัง","พูด"), "อ่าน"),
        Question(75, "Write",     "เขียน",      listOf("อ่าน","เขียน","วาด","ลบ"), "เขียน"),
        Question(76, "Study",     "เรียน",      listOf("สอน","เรียน","สอบ","เล่น"), "เรียน"),
        Question(77, "Play",      "เล่น",       listOf("ทำงาน","เล่น","พัก","ฝึก"), "เล่น"),
        Question(78, "Speak",     "พูด",        listOf("ฟัง","พูด","ร้องเพลง","กรีดร้อง"), "พูด"),
        Question(79, "Listen",    "ฟัง",        listOf("พูด","ดู","ฟัง","สัมผัส"), "ฟัง"),
        Question(80, "Walk",      "เดิน",       listOf("วิ่ง","เดิน","ขับรถ","ขี่จักรยาน"), "เดิน"),
        Question(81, "Jump",      "กระโดด",     listOf("นั่ง","กระโดด","คลาน","ล้ม"), "กระโดด"),
        Question(82, "Think",     "คิด",        listOf("รู้สึก","คิด","ฝัน","จำ"), "คิด"),
        Question(83, "Know",      "รู้",        listOf("เข้าใจ","รู้","ลืม","จำ"), "รู้"),
        Question(84, "Believe",   "เชื่อ",      listOf("สงสัย","เชื่อ","กลัว","หวัง"), "เชื่อ"),
        Question(85, "Create",    "สร้าง",      listOf("ทำลาย","สร้าง","ซ่อม","เปลี่ยน"), "สร้าง"),
        Question(86, "Destroy",   "ทำลาย",      listOf("สร้าง","ทำลาย","ซ่อม","รักษา"), "ทำลาย"),
        Question(87, "Discover",  "ค้นพบ",      listOf("ซ่อน","ค้นพบ","ลืม","หาย"), "ค้นพบ"),
        Question(88, "Protect",   "ปกป้อง",     listOf("โจมตี","ปกป้อง","หนี","ต่อสู้"), "ปกป้อง"),
        Question(89, "Freedom",   "อิสรภาพ",    listOf("ความยุติธรรม","อิสรภาพ","ความสงบ","อำนาจ"), "อิสรภาพ"),
        Question(90, "Wisdom",    "ปัญญา",      listOf("ความรู้","ปัญญา","พลัง","ความเร็ว"), "ปัญญา"),
        Question(91, "Courage",   "ความกล้า",   listOf("ความกลัว","ความกล้า","ความโกรธ","ความเศร้า"), "ความกล้า"),
        Question(92, "Promise",   "สัญญา",      listOf("คำขอ","สัญญา","คำสั่ง","คำเตือน"), "สัญญา"),
        Question(93, "Adventure", "การผจญภัย",  listOf("การพักผ่อน","การผจญภัย","การเดินทาง","การทำงาน"), "การผจญภัย"),
        Question(94, "Challenge", "ความท้าทาย", listOf("ปัญหา","ความท้าทาย","อุปสรรค","ความยาก"), "ความท้าทาย"),
        Question(95, "Victory",   "ชัยชนะ",     listOf("ความพ่ายแพ้","ชัยชนะ","การแข่งขัน","รางวัล"), "ชัยชนะ")
    )

    // ── Question pools by rarity ────────────────────────────────────────────
    val commonQuestions    get() = questions.filter { it.id in 1..15 }
    val rareQuestions      get() = questions.filter { it.id in 16..30 }
    val epicQuestions      get() = questions.filter { it.id in 31..50 }
    val legendaryQuestions get() = questions.filter { it.id in 51..70 }
    val mythicQuestions    get() = questions.filter { it.id in 71..95 }

    fun questionsForTier(tier: DifficultyTier): List<Question> = when (tier) {
        DifficultyTier.COMMON    -> commonQuestions
        DifficultyTier.RARE      -> rareQuestions
        DifficultyTier.EPIC      -> epicQuestions
        DifficultyTier.LEGENDARY -> legendaryQuestions
        DifficultyTier.MYTHIC    -> mythicQuestions
    }

    // ── Boss Pokemon — 5 ระดับ Rarity ───────────────────────────────────────
    val bosses = listOf(
        // COMMON — จับง่าย, เล่นมินิเกม 1 รอบ
        BossEnemy(25,  "Pikachu",    DifficultyTier.COMMON, "Electric", 0xFFFDD835, 0xFFF9A825, 50),
        BossEnemy(133, "Eevee",      DifficultyTier.COMMON, "Normal",   0xFFD7A96D, 0xFFB8860B, 50),
        BossEnemy(39,  "Jigglypuff", DifficultyTier.COMMON, "Normal",   0xFFFF80AB, 0xFFFF4081, 50),
        // RARE — เล่นมินิเกม 2 รอบติดต่อกัน
        BossEnemy(6,   "Charizard",  DifficultyTier.RARE, "Fire",    0xFFFF7043, 0xFFE64A19, 80),
        BossEnemy(9,   "Blastoise",  DifficultyTier.RARE, "Water",   0xFF42A5F5, 0xFF1565C0, 80),
        BossEnemy(59,  "Arcanine",   DifficultyTier.RARE, "Fire",    0xFFFF8A65, 0xFFBF360C, 80),
        // EPIC — เล่นมินิเกม 3 รอบติดต่อกัน
        BossEnemy(94,  "Gengar",     DifficultyTier.EPIC, "Ghost",   0xFF9C27B0, 0xFF6A1B9A, 120),
        BossEnemy(65,  "Alakazam",   DifficultyTier.EPIC, "Psychic", 0xFFFFF176, 0xFFF9A825, 120),
        BossEnemy(68,  "Machamp",    DifficultyTier.EPIC, "Fighting",0xFF78909C, 0xFF455A64, 120),
        // LEGENDARY — เล่นมินิเกม 4 รอบติดต่อกัน
        BossEnemy(150, "Mewtwo",     DifficultyTier.LEGENDARY, "Psychic", 0xFF9C27B0, 0xFF4A148C, 180),
        BossEnemy(149, "Dragonite",  DifficultyTier.LEGENDARY, "Dragon",  0xFFFFB74D, 0xFFEF6C00, 180),
        BossEnemy(248, "Tyranitar",  DifficultyTier.LEGENDARY, "Rock",    0xFF546E7A, 0xFF263238, 180),
        // MYTHIC — เล่นมินิเกม 5 รอบติดต่อกัน
        BossEnemy(384, "Rayquaza",   DifficultyTier.MYTHIC, "Dragon", 0xFF66BB6A, 0xFF2E7D32, 250),
        BossEnemy(487, "Giratina",   DifficultyTier.MYTHIC, "Ghost",  0xFF546E7A, 0xFF37474F, 250),
        BossEnemy(493, "Arceus",     DifficultyTier.MYTHIC, "Normal", 0xFFFFD54F, 0xFFF9A825, 250)
    )

    val initialPlayerStats = PlayerStats(
        name = "Player 1",
        level = 1,
        totalXP = 0,
        wordsLearned = 0,
        totalScore = 0,
        linguamonCollected = emptyList()
    )
}
