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
