# Edumension 📚✨

แอปพลิเคชัน Android สำหรับเรียนรู้คำศัพท์ภาษาอังกฤษ-ไทย ผ่านระบบเกม RPG สะสมสัตว์แฟนตาซี **Linguamon**

---

## 🎨 UI / Wireframe Design

ออกแบบ UI ด้วย Figma:

🔗 [ดู Wireframe บน Figma](https://www.figma.com/design/zkSMVlFPVG9O8tsNQtXxWp/Wireframe-Hod?node-id=0-1&t=dTZ0tBrqZI6SuBco-1)

---

## 📖 ภาพรวมของแอป

**Edumension** ผสานการเรียนรู้คำศัพท์เข้ากับระบบเกมสะสมสัตว์คล้าย Pokémon โดยผู้เล่นจะต้องตอบคำถามคำศัพท์ให้ถูกต้องเพื่อต่อสู้และจับ **Linguamon** ซึ่งเป็นสัตว์แฟนตาซีในโลกของแอป

### ฟีเจอร์หลัก

| ฟีเจอร์ | รายละเอียด |
|---|---|
| 🏠 หน้าหลัก (Home) | เมนูเริ่มเกม, ดู Collection, ดูสถิติ |
| 🎮 โหมด Boss Battle | ตอบคำถามเพื่อต่อสู้ Boss Linguamon และจับให้ได้ |
| 🏋️ โหมด Training | ฝึกทบทวนคำศัพท์โดยไม่มีการจับ Linguamon |
| 🎯 Catch Phase | มินิเกมโยน Pokéball หลังจากตอบถูกครบทุกข้อ |
| 🏆 Collection | ดูและจัดการ Linguamon ที่สะสมไว้ |
| 📊 Stats | ดูสถิติการเล่นและความก้าวหน้า |

### ระดับความยาก (Difficulty Tier)

| Tier | สี | รอบ Catch | จำนวนคำถาม |
|---|---|---|---|
| Common | 🟢 | 3 | 5 |
| Rare | 🔵 | 3 | 6 |
| Epic | 🟣 | 3 | 7 |
| Legendary | 🟡 | 3 | 8 |
| Mythic | 🔴 | 3 | 10 |

---

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose + Material 3
- **Architecture:** ViewModel (MVVM)
- **Navigation:** Navigation Compose
- **Networking:** Retrofit 2 + Gson
- **Image Loading:** Coil for Compose
- **Icons:** Material Icons Extended
- **Pokémon Sprites:** [PokeAPI CDN](https://raw.githubusercontent.com/PokeAPI/sprites/)

---

## 📋 Requirements

| รายการ | เวอร์ชัน |
|---|---|
| Minimum SDK | Android 7.0 (API 24) |
| Target SDK | API 36 |
| Compile SDK | API 36 |
| App Version | 1.0 |

---

## 🚀 วิธีติดตั้งและรันโปรเจกต์

1. **Clone repository**
   ```bash
   git clone https://github.com/pakornkiatjims-swu/Edumension.git
   ```

2. **เปิดด้วย Android Studio**
   - ใช้ Android Studio Hedgehog หรือใหม่กว่า
   - เปิดโฟลเดอร์ที่ clone มา

3. **Sync Gradle**
   - กด **Sync Now** เมื่อ Android Studio แจ้งเตือน

4. **Run แอป**
   - เชื่อมต่ออุปกรณ์ Android หรือเปิด Emulator
   - กด **Run ▶** หรือ `Shift+F10`

---

## 📁 โครงสร้างโปรเจกต์

```
app/src/main/java/com/example/edumension/
├── MainActivity.kt
├── data/
│   ├── Models.kt          # Data classes: Question, Linguamon, BossEnemy, ...
│   ├── MockData.kt        # ข้อมูลตัวอย่าง
│   └── remote/
│       ├── PokeApiService.kt
│       └── PokemonResponse.kt
├── ui/
│   ├── GameViewModel.kt   # State management หลักของเกม
│   ├── components/
│   │   └── LinguamonMascot.kt
│   ├── navigation/
│   │   └── AppNavigation.kt
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── GameScreen.kt
│   │   ├── CatchPhaseScreen.kt
│   │   ├── ResultScreen.kt
│   │   ├── TrainingResultScreen.kt
│   │   ├── CollectionScreen.kt
│   │   ├── StatsScreen.kt
│   │   └── LinguamonDetailSheet.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
```

---

## 🤝 Contributors

- **pakornkiatjims-swu** — Developer & Designer
