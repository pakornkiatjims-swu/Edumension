package com.example.edumension.ui

import androidx.lifecycle.ViewModel
import com.example.edumension.data.MockData
import com.example.edumension.data.PlayerStats
import com.example.edumension.data.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _playerStats = MutableStateFlow(MockData.initialPlayerStats)
    val playerStats: StateFlow<PlayerStats> = _playerStats.asStateFlow()

    private val allQuestions = MockData.questions.shuffled()
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _currentQuestion = MutableStateFlow(allQuestions.first())
    val currentQuestion: StateFlow<Question> = _currentQuestion.asStateFlow()

    private val _currentScore = MutableStateFlow(0)
    val currentScore: StateFlow<Int> = _currentScore.asStateFlow()

    private val _currentXP = MutableStateFlow(0)
    val currentXP: StateFlow<Int> = _currentXP.asStateFlow()

    val totalQuestions = allQuestions.size

    fun submitAnswer(selectedIndex: Int): Boolean {
        val isCorrect = selectedIndex == currentQuestion.value.correctIndex
        if (isCorrect) {
            _currentScore.update { it + 10 }
            _currentXP.update { it + 20 }
        }
        return isCorrect
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < allQuestions.size - 1) {
            _currentQuestionIndex.update { it + 1 }
            _currentQuestion.value = allQuestions[_currentQuestionIndex.value]
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
        _currentQuestionIndex.value = 0
        _currentScore.value = 0
        _currentXP.value = 0
        _currentQuestion.value = allQuestions.first()
    }
}
