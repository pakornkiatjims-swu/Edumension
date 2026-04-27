package com.example.edumension.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        fetchPokemonData()
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
                
                _playerStats.update {
                    it.copy(linguamonCollected = fetchedPokemons)
                }
            } catch (e: Exception) {
                // Keep the initial mock data on error
                e.printStackTrace()
            }
        }
    }

    // สุ่มโจทย์ใหม่ทุกครั้งที่เริ่มเกม — เลือก QUESTIONS_PER_GAME ข้อจากคลังทั้งหมด
    private var sessionQuestions: List<Question> = pickRandomQuestions()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _currentQuestion = MutableStateFlow(sessionQuestions.first())
    val currentQuestion: StateFlow<Question> = _currentQuestion.asStateFlow()

    private val _currentScore = MutableStateFlow(0)
    val currentScore: StateFlow<Int> = _currentScore.asStateFlow()

    private val _currentXP = MutableStateFlow(0)
    val currentXP: StateFlow<Int> = _currentXP.asStateFlow()

    /** จำนวนข้อในรอบปัจจุบัน */
    val totalQuestions: Int get() = sessionQuestions.size

    private fun pickRandomQuestions(): List<Question> =
        MockData.questions.shuffled().take(MockData.QUESTIONS_PER_GAME)

    fun submitAnswer(selectedIndex: Int): Boolean {
        val isCorrect = selectedIndex == currentQuestion.value.correctIndex
        if (isCorrect) {
            _currentScore.update { it + 10 }
            _currentXP.update { it + 20 }
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
        // สุ่มชุดโจทย์ใหม่ทุกครั้ง
        sessionQuestions = pickRandomQuestions()
        _currentQuestionIndex.value = 0
        _currentScore.value = 0
        _currentXP.value = 0
        _currentQuestion.value = sessionQuestions.first()
    }
}
