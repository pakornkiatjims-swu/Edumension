package com.example.edumension.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edumension.data.BossEnemy
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

    /** HP เหลือของ boss (ลดทุกครั้งที่ตอบถูก) */
    private val _bossHp = MutableStateFlow(_currentBoss.value.hp)
    val bossHp: StateFlow<Int> = _bossHp.asStateFlow()

    val bossMaxHp: Int get() = _currentBoss.value.hp

    private fun pickRandomBoss(): BossEnemy = MockData.bosses.random()

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

    /** สุ่มคำถามจาก pool ที่ตรงกับ tier ของ boss */
    private fun pickQuestionsForBoss(boss: BossEnemy): List<Question> {
        val pool = when (boss.tier) {
            DifficultyTier.EASY   -> MockData.easyQuestions
            DifficultyTier.MEDIUM -> MockData.mediumQuestions
            DifficultyTier.HARD   -> MockData.hardQuestions
        }
        // ถ้า pool ในระดับนั้นมีน้อยกว่า QUESTIONS_PER_GAME ก็ใช้ทั้งหมด
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
                val colorsEnd = listOf(0xFF10B981, 0xFFF44336, 0xFF00BCD4)
                val icons = listOf("🍃", "🔥", "💧")

                val fetchedPokemons = pokemonNames.mapIndexed { index, name ->
                    val response = RetrofitClient.instance.getPokemon(name)
                    Linguamon(
                        id = response.id,
                        name = response.name.replaceFirstChar { it.uppercase() },
                        type = response.types.firstOrNull()?.type?.name ?: "Unknown",
                        colorStart = colorsStart[index],
                        colorEnd = colorsEnd[index],
                        level = 5,
                        xp = response.baseExperience,
                        icon = icons[index],
                        description = "A friendly ${response.types.firstOrNull()?.type?.name} type Pokemon.",
                        imageUrl = response.sprites.other.officialArtwork.frontDefault
                    )
                }

                _playerStats.update { it.copy(linguamonCollected = fetchedPokemons) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /** ดึงรูป boss จาก PokeAPI และ update imageUrl */
    private fun fetchBossImage(boss: BossEnemy) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getPokemon(boss.pokemonId.toString())
                val imageUrl = response.sprites.other.officialArtwork.frontDefault
                _currentBoss.update { it.copy(imageUrl = imageUrl) }
                _bossHp.value = boss.hp
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ── Game Logic ────────────────────────────────────────────────────────────

    /**
     * ส่งคำตอบ → คืน true/false
     * หากถูก: +score +xp และ damage boss HP
     */
    fun submitAnswer(selectedIndex: Int): Boolean {
        val isCorrect = selectedIndex == currentQuestion.value.correctIndex
        if (isCorrect) {
            _currentScore.update { it + 10 }
            _currentXP.update { it + 20 }
            // ลด HP boss ต่อคำถาม = bossMaxHp / totalQuestions (rounded)
            val dmg = (_currentBoss.value.hp.toFloat() / totalQuestions).toInt().coerceAtLeast(1)
            _bossHp.update { (it - dmg).coerceAtLeast(0) }
        }
        return isCorrect
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < sessionQuestions.size - 1) {
            _currentQuestionIndex.update { it + 1 }
            _currentQuestion.value = sessionQuestions[_currentQuestionIndex.value]
        }
    }

    fun skipQuestion() {
        nextQuestion()
    }

    fun finishGame() {
        _playerStats.update {
            it.copy(
                totalScore = it.totalScore + _currentScore.value,
                totalXP = it.totalXP + _currentXP.value
            )
        }
    }

    fun resetGame() {
        // สุ่ม boss ใหม่และดึงรูปใหม่
        val newBoss = pickRandomBoss()
        _currentBoss.value = newBoss
        fetchBossImage(newBoss)

        // สุ่มชุดโจทย์ตาม boss tier ใหม่
        sessionQuestions = pickQuestionsForBoss(newBoss)
        _currentQuestionIndex.value = 0
        _currentScore.value = 0
        _currentXP.value = 0
        _currentQuestion.value = sessionQuestions.first()
    }
}
