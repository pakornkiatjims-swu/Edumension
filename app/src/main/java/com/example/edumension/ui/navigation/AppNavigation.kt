package com.example.edumension.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.screens.CatchPhaseScreen
import com.example.edumension.ui.screens.CollectionScreen
import com.example.edumension.ui.screens.GameScreen
import com.example.edumension.ui.screens.HomeScreen
import com.example.edumension.ui.screens.ResultScreen
import com.example.edumension.ui.screens.StatsScreen
import com.example.edumension.ui.screens.TrainingResultScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onStartGame = {
                    gameViewModel.resetGame()
                    navController.navigate("game")
                },
                onViewCollection = { navController.navigate("collection") },
                onViewStats = { navController.navigate("stats") }
            )
        }
        composable("game") {
            GameScreen(
                viewModel = gameViewModel,
                onGameFinished = { shouldCatch ->
                    gameViewModel.finishGame()
                    if (shouldCatch) {
                        // Boss mode + ตอบถูกหมด → Catch Phase
                        gameViewModel.startCatchPhase()
                        navController.navigate("catch_phase") {
                            popUpTo("home") { inclusive = false }
                        }
                    } else {
                        // Boss mode ผิดบางข้อ OR Training mode → Result
                        val destination = if (gameViewModel.gameMode.value.name == "TRAINING")
                            "training_result" else "result"
                        navController.navigate(destination) {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                }
            )
        }
        composable("catch_phase") {
            CatchPhaseScreen(
                viewModel = gameViewModel,
                onDone = {
                    navController.navigate("result") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }
        composable("result") {
            ResultScreen(
                viewModel = gameViewModel,
                onPlayAgain = {
                    gameViewModel.resetGame()
                    navController.navigate("game") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onBackHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
        composable("training_result") {
            TrainingResultScreen(
                viewModel = gameViewModel,
                onDone = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
        composable("stats") {
            StatsScreen(
                viewModel = gameViewModel,
                onBackHome = { navController.popBackStack() }
            )
        }
        composable("collection") {
            CollectionScreen(
                viewModel = gameViewModel,
                onBackHome = { navController.popBackStack() },
                onStartTraining = {
                    navController.navigate("game") {
                        popUpTo("collection") { inclusive = false }
                    }
                }
            )
        }
    }
}
