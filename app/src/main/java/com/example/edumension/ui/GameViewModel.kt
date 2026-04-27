package com.example.edumension.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edumension.data.BossEnemy
import com.example.edumension.data.CatchablePokemon
import com.example.edumension.data.DifficultyTier
import com.example.edumension.data.Linguamon
import com.example.edumension.data.MockData
import com.example.edumension.data.PlayerStats
import com.example.edumension.data.Question
import com.example.edumension.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _playerStats = MutableStateFlow(MockData.initialPlayerStats)
    val playerStats: StateFlow<PlayerStats> = _playerStats.asStateFlow()

    // ── Boss ──────────────────────────────────────────────────────────────────

    private val _currentBoss = MutableStateFlow(pickRandomBoss())
    val currentBoss: StateFlow<BossEnemy> = _currentBoss.asStateFlow()

    private val _bossHp = MutableStateFlow(_currentBoss.value.hp)
    val bossHp: StateFlow<Int> = _bossHp.asStateFlow()

    val bossMaxHp: Int get() = _currentBoss.value.hp

    private fun pickRandomBoss(): BossEnemy = MockData.bosses.random()

    // ── Catch Mini-Game ───────────────────────────────────────────────────────

    /**
     * เมื่อ != null → แสดง CatchMinigameScreen overlay
     * ตั้งค่าเมื่อผู้ใช้ตอบถูก, reset หลัง dismiss
     */
    private val _pendingCatch = MutableStateFlow<CatchablePokemon?>(null)
    val pendingCatch: StateFlow<CatchablePokemon?> = _pendingCatch.asStateFlow()

    /** เรียกเมื่อจับ Pokemon สำเร็จ — เพิ่มเข้า Collection */
    fun onCatchSuccess() {
        val pokemon = _pendingCatch.value ?: return
        val newLinguamon = pokemon.toLinguamon()
        _playerStats.update { stats ->
            // ไม่เพิ่มซ้ำถ้ามีอยู่แล้ว
            if (stats.linguamonCollected.any { it.id == newLinguamon.id }) stats
            else stats.copy(linguamonCollected = stats.linguamonCollected + newLinguamon)
        }
        _pendingCatch.value = null
    }

    /** เรียกเมื่อจับไม่สำเร็จหรือ timeout */
    fun onCatchDismissed() {
        _pendingCatch.value = null
    }

    // ── Questions ─────────────────────────────────────────────────────────────

    private var sessionQuestions: List<Question> = pickQuestionsForBoss(_currentBoss.value)

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _currentQuestion = MutableStateFlow(sessionQuestions.first())
    val currentQuestion: StateFlow<Question> = _currentQuestion.asStateFlow()

    private val _currentScore = MutableStateFlow(0)
    val currentScore: StateFlow<Int> = _currentScore.asStateFlow()

    private val _currentXP = MutableStateFlow(0)
    val currentXP: StateFlow<Int> = _currentXP.asStateFlow()

    val totalQuestions: Int get() = sessionQuestions.size

    private fun pickQuestionsForBoss(boss: BossEnemy): List<Question> {
        val pool = when (boss.tier) {
            DifficultyTier.EASY   -> MockData.easyQuestions
            DifficultyTier.MEDIUM -> MockData.mediumQuestions
            DifficultyTier.HARD   -> MockData.hardQuestions
        }
        return pool.shuffled().take(MockData.QUESTIONS_PER_GAME)
    }

    // ── Lifecycle Init ────────────────────────────────────────────────────────

    init {
        fetchPokemonData()
        fetchBossImage(_currentBoss.value)
    }

    private fun fetchPokemonData() {
        viewModelScope.launch {
            try {
                val pokemonNames = listOf("bulbasaur", "charmander", "squirtle")
                val colorsStart = listOf(0xFF66BB6A, 0xFFFFA726, 0xFF42A5F5)
                val colorsEnd   = listOf(0xFF10B981, 0xFFF44336, 0xFF00BCD4)
                val icons       = listOf("🍃", "🔥", "💧")

                val fetched = pokemonNames.mapIndexed { i, name ->
                    val r = RetrofitClient.instance.getPokemon(name)
                    Linguamon(
                        id = r.id,
                        name = r.name.replaceFirstChar { it.uppercase() },
                        type = r.types.firstOrNull()?.type?.name ?: "Unknown",
                        colorStart = colorsStart[i],
                        colorEnd   = colorsEnd[i],
                        level = 5,
                        xp    = r.baseExperience,
                        icon  = icons[i],
                        description = "A friendly ${r.types.firstOrNull()?.type?.name} type Pokemon.",
                        imageUrl = r.sprites.other.officialArtwork.frontDefault
                    )
                }
                // เราไม่โหลด mock linguamon เข้า collection แล้ว — ใช้ที่ดึงมาสำหรับ reference เท่านั้น
                // _playerStats.update { it.copy(linguamonCollected = fetched) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchBossImage(boss: BossEnemy) {
        viewModelScope.launch {
            try {
                val r = RetrofitClient.instance.getPokemon(boss.pokemonId.toString())
                _currentBoss.update { it.copy(imageUrl = r.sprites.other.officialArtwork.frontDefault) }
                _bossHp.value = boss.hp
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ── Game Logic ────────────────────────────────────────────────────────────

    /**
     * ส่งคำตอบ → คืน true/false
     * ถูก: +score, +xp, damage boss, สุ่ม Pokemon ให้จับ
     */
    fun submitAnswer(selectedIndex: Int): Boolean {
        val isCorrect = selectedIndex == currentQuestion.value.correctIndex
        if (isCorrect) {
            _currentScore.update { it + 10 }
            _currentXP.update { it + 20 }
            val dmg = (_currentBoss.value.hp.toFloat() / totalQuestions).toInt().coerceAtLeast(1)
            _bossHp.update { (it - dmg).coerceAtLeast(0) }

            // สุ่ม Pokemon จาก pool ตาม tier ของ boss
            val tierPool = MockData.catchablePokemons.filter { it.tier == _currentBoss.value.tier }
            _pendingCatch.value = tierPool.randomOrNull()
        }
        return isCorrect
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < sessionQuestions.size - 1) {
            _currentQuestionIndex.update { it + 1 }
            _currentQuestion.value = sessionQuestions[_currentQuestionIndex.value]
        }
    }

    fun skipQuestion() = nextQuestion()

    fun finishGame() {
        _playerStats.update {
            it.copy(
                totalScore = it.totalScore + _currentScore.value,
                totalXP    = it.totalXP + _currentXP.value
            )
        }
    }

    fun resetGame() {
        val newBoss = pickRandomBoss()
        _currentBoss.value = newBoss
        fetchBossImage(newBoss)
        sessionQuestions = pickQuestionsForBoss(newBoss)
        _currentQuestionIndex.value = 0
        _currentScore.value = 0
        _currentXP.value = 0
        _currentQuestion.value = sessionQuestions.first()
        _pendingCatch.value = null
    }
}
