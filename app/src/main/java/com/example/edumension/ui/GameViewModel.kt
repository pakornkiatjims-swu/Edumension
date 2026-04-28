package com.example.edumension.ui

import androidx.lifecycle.ViewModel
import com.example.edumension.data.BossEnemy
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

    /** จำนวนข้อที่ตอบถูก */
    private val _correctCount = MutableStateFlow(0)
    val correctCount: StateFlow<Int> = _correctCount.asStateFlow()

    /** ตอบถูกทุกข้อหรือไม่ */
    val allCorrect: Boolean get() = _correctCount.value == totalQuestions

    // ── Game Phase ────────────────────────────────────────────────────────────
    private val _gamePhase = MutableStateFlow(GamePhase.QUIZ)
    val gamePhase: StateFlow<GamePhase> = _gamePhase.asStateFlow()

    // ── Catch Phase ───────────────────────────────────────────────────────────
    /** รอบมินิเกมปัจจุบัน (0-indexed) */
    private val _catchRound = MutableStateFlow(0)
    val catchRound: StateFlow<Int> = _catchRound.asStateFlow()

    /** จำนวนรอบทั้งหมดที่ต้องผ่าน */
    val totalCatchRounds: Int get() = _currentBoss.value.tier.catchRounds

    /** ผลลัพธ์สุดท้ายของการจับ */
    private val _catchResult = MutableStateFlow(CatchResult.NONE)
    val catchResult: StateFlow<CatchResult> = _catchResult.asStateFlow()

    private fun pickQuestionsForBoss(boss: BossEnemy): List<Question> {
        val pool = MockData.questionsForTier(boss.tier)
        return pool.shuffled().take(boss.tier.questionsPerGame)
    }

    // ── Game Logic ────────────────────────────────────────────────────────────

    fun submitAnswer(selectedIndex: Int): Boolean {
        val isCorrect = selectedIndex == currentQuestion.value.correctIndex
        if (isCorrect) {
            _currentScore.update { it + 10 }
            _currentXP.update { it + 20 }
            _correctCount.update { it + 1 }
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

    fun skipQuestion() = nextQuestion()

    fun finishGame() {
        _playerStats.update {
            it.copy(
                totalScore = it.totalScore + _currentScore.value,
                totalXP = it.totalXP + _currentXP.value
            )
        }
    }

    // ── Catch Phase Logic ─────────────────────────────────────────────────────

    /** เริ่ม catch phase — เรียกเมื่อตอบถูกหมดทุกข้อ */
    fun startCatchPhase() {
        _gamePhase.value = GamePhase.CATCH
        _catchRound.value = 0
        _catchResult.value = CatchResult.NONE
    }

    /** เรียกเมื่อผ่านมินิเกมรอบนั้นสำเร็จ */
    fun onCatchRoundSuccess() {
        val nextRound = _catchRound.value + 1
        if (nextRound >= totalCatchRounds) {
            // ผ่านครบทุกรอบ → จับ Boss สำเร็จ!
            val boss = _currentBoss.value
            val newLinguamon = boss.toLinguamon()
            _playerStats.update { stats ->
                if (stats.linguamonCollected.any { it.id == newLinguamon.id }) stats
                else stats.copy(linguamonCollected = stats.linguamonCollected + newLinguamon)
            }
            _catchResult.value = CatchResult.SUCCESS
            _gamePhase.value = GamePhase.RESULT
        } else {
            // ยังไม่ครบ → ไปรอบถัดไป
            _catchRound.value = nextRound
        }
    }

    /** เรียกเมื่อพลาดมินิเกม → Boss หนีไป */
    fun onCatchRoundFail() {
        _catchResult.value = CatchResult.FAILED
        _gamePhase.value = GamePhase.RESULT
    }

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
    }
}
