package com.example.edumension.ui

import androidx.lifecycle.ViewModel
import com.example.edumension.data.BossEnemy
import com.example.edumension.data.Linguamon
import com.example.edumension.data.MockData
import com.example.edumension.data.PlayerStats
import com.example.edumension.data.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** สถานะหลักของเกม */
enum class GamePhase { QUIZ, CATCH, RESULT }

/** ผลลัพธ์การจับ Boss */
enum class CatchResult { NONE, SUCCESS, FAILED }

/** Mode การเล่น — Boss Quiz vs Training Quiz */
enum class GameMode { BOSS, TRAINING }

class GameViewModel : ViewModel() {

    private val _playerStats = MutableStateFlow(MockData.initialPlayerStats)
    val playerStats: StateFlow<PlayerStats> = _playerStats.asStateFlow()

    // ── Mode ──────────────────────────────────────────────────────────────────
    private val _gameMode = MutableStateFlow(GameMode.BOSS)
    val gameMode: StateFlow<GameMode> = _gameMode.asStateFlow()

    // ── Training target — Linguamon ที่กำลัง train ───────────────────────────
    private val _trainTarget = MutableStateFlow<Linguamon?>(null)
    val trainTarget: StateFlow<Linguamon?> = _trainTarget.asStateFlow()

    // ── Boss ──────────────────────────────────────────────────────────────────
    private val _currentBoss = MutableStateFlow(pickRandomBoss())
    val currentBoss: StateFlow<BossEnemy> = _currentBoss.asStateFlow()

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

    private val _correctCount = MutableStateFlow(0)
    val correctCount: StateFlow<Int> = _correctCount.asStateFlow()

    val allCorrect: Boolean get() = _correctCount.value == totalQuestions

    // ── Game Phase ────────────────────────────────────────────────────────────
    private val _gamePhase = MutableStateFlow(GamePhase.QUIZ)
    val gamePhase: StateFlow<GamePhase> = _gamePhase.asStateFlow()

    // ── Catch Phase ───────────────────────────────────────────────────────────
    private val _catchRound = MutableStateFlow(0)
    val catchRound: StateFlow<Int> = _catchRound.asStateFlow()

    val totalCatchRounds: Int get() = _currentBoss.value.tier.catchRounds

    private val _catchResult = MutableStateFlow(CatchResult.NONE)
    val catchResult: StateFlow<CatchResult> = _catchResult.asStateFlow()

    private fun pickQuestionsForBoss(boss: BossEnemy): List<Question> {
        val pool = MockData.questionsForTier(boss.tier)
        return pool.shuffled().take(boss.tier.questionsPerGame)
    }

    private fun pickTrainingQuestions(): List<Question> =
        MockData.questions.shuffled().take(TRAINING_QUESTIONS)

    // ── Boss Game Logic ───────────────────────────────────────────────────────

    fun submitAnswer(selectedIndex: Int): Boolean {
        val isCorrect = selectedIndex == currentQuestion.value.correctIndex
        if (isCorrect) {
            _currentScore.update { it + 10 }
            _currentXP.update { it + 20 }
            _correctCount.update { it + 1 }
            if (_gameMode.value == GameMode.BOSS) {
                val dmg = (_currentBoss.value.hp.toFloat() / totalQuestions).toInt().coerceAtLeast(1)
                _bossHp.update { (it - dmg).coerceAtLeast(0) }
            }
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
                totalXP = it.totalXP + _currentXP.value
            )
        }
        // ถ้า Training mode → reward XP ให้ Linguamon ที่เลือก
        if (_gameMode.value == GameMode.TRAINING) {
            val xpGained = _correctCount.value * TRAINING_XP_PER_CORRECT
            _trainTarget.value?.let { target ->
                addXpToLinguamon(target.id, xpGained)
            }
        }
    }

    // ── Catch Phase Logic ─────────────────────────────────────────────────────

    fun startCatchPhase() {
        _gamePhase.value = GamePhase.CATCH
        _catchRound.value = 0
        _catchResult.value = CatchResult.NONE
    }

    fun onCatchRoundSuccess() {
        val nextRound = _catchRound.value + 1
        if (nextRound >= totalCatchRounds) {
            val boss = _currentBoss.value
            val newLinguamon = boss.toLinguamon()
            _playerStats.update { stats ->
                if (stats.linguamonCollected.any { it.id == newLinguamon.id }) {
                    // จับซ้ำ → เพิ่ม timeCaught
                    stats.copy(linguamonCollected = stats.linguamonCollected.map {
                        if (it.id == newLinguamon.id) it.copy(timeCaught = it.timeCaught + 1) else it
                    })
                } else {
                    stats.copy(linguamonCollected = stats.linguamonCollected + newLinguamon)
                }
            }
            _catchResult.value = CatchResult.SUCCESS
            _gamePhase.value = GamePhase.RESULT
        } else {
            _catchRound.value = nextRound
        }
    }

    fun onCatchRoundFail() {
        _catchResult.value = CatchResult.FAILED
        _gamePhase.value = GamePhase.RESULT
    }

    // ── Training System ───────────────────────────────────────────────────────

    /** เริ่ม Training Session สำหรับ Linguamon ที่เลือก */
    fun startTraining(linguamon: Linguamon) {
        _trainTarget.value = linguamon
        _gameMode.value = GameMode.TRAINING
        sessionQuestions = pickTrainingQuestions()
        _currentQuestionIndex.value = 0
        _currentQuestion.value = sessionQuestions.first()
        _currentScore.value = 0
        _currentXP.value = 0
        _correctCount.value = 0
        _gamePhase.value = GamePhase.QUIZ
        _catchResult.value = CatchResult.NONE
    }

    // ── Linguamon Collection Management ──────────────────────────────────────

    /** เพิ่ม XP ให้ Linguamon ตาม id — level up อัตโนมัติทุก 1000 XP */
    fun addXpToLinguamon(id: Int, amount: Int) {
        _playerStats.update { stats ->
            stats.copy(linguamonCollected = stats.linguamonCollected.map { l ->
                if (l.id != id) return@map l
                val newXp = l.xp + amount
                val levelsGained = newXp / XP_PER_LEVEL - l.xp / XP_PER_LEVEL
                l.copy(xp = newXp, level = l.level + levelsGained)
            })
        }
        // sync trainTarget ถ้าเป็นตัวเดียวกัน
        _trainTarget.update { t ->
            if (t?.id == id) _playerStats.value.linguamonCollected.find { it.id == id }
            else t
        }
    }

    /** วิวัฒนาการ Linguamon */
    fun evolveLinguamon(id: Int) {
        val target = _playerStats.value.linguamonCollected.find { it.id == id } ?: return
        val evoId = target.evolutionId ?: return
        // หา boss ที่ตรงกับ evolutionId เป็น template ของรูปร่างใหม่
        val template = MockData.bosses.find { it.pokemonId == evoId }
        _playerStats.update { stats ->
            stats.copy(linguamonCollected = stats.linguamonCollected.map { l ->
                if (l.id != id) l
                else l.copy(
                    id = evoId,
                    name = template?.name ?: l.name,
                    imageUrl = template?.imageUrl ?: l.imageUrl,
                    isEvolved = true,
                    evolutionId = null
                )
            })
        }
    }

    /** ปล่อย Linguamon ออกจาก collection */
    fun releaseLinguamon(id: Int) {
        _playerStats.update { stats ->
            stats.copy(linguamonCollected = stats.linguamonCollected.filter { it.id != id })
        }
        if (_trainTarget.value?.id == id) _trainTarget.value = null
    }

    // ── Reset (Boss mode) ─────────────────────────────────────────────────────

    fun resetGame() {
        val newBoss = pickRandomBoss()
        _currentBoss.value = newBoss
        sessionQuestions = pickQuestionsForBoss(newBoss)
        _currentQuestionIndex.value = 0
        _currentScore.value = 0
        _currentXP.value = 0
        _correctCount.value = 0
        _bossHp.value = newBoss.hp
        _currentQuestion.value = sessionQuestions.first()
        _gamePhase.value = GamePhase.QUIZ
        _catchRound.value = 0
        _catchResult.value = CatchResult.NONE
        _gameMode.value = GameMode.BOSS
        _trainTarget.value = null
    }

    companion object {
        const val XP_PER_LEVEL = 1000
        const val TRAINING_QUESTIONS = 5
        const val TRAINING_XP_PER_CORRECT = 50   // ตอบถูก 5/5 = 250 XP
    }
}
