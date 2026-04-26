package com.example.edumension.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.edumension.ui.GameViewModel
import com.example.edumension.ui.screens.CollectionScreen
import com.example.edumension.ui.screens.GameScreen
import com.example.edumension.ui.screens.HomeScreen
import com.example.edumension.ui.screens.ResultScreen
import com.example.edumension.ui.screens.StatsScreen

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
                onGameFinished = {
                    gameViewModel.finishGame()
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
        composable("stats") {
            StatsScreen(
                viewModel = gameViewModel,
                onBackHome = { navController.popBackStack() }
            )
        }
        composable("collection") {
            CollectionScreen(
                viewModel = gameViewModel,
                onBackHome = { navController.popBackStack() }
            )
        }
    }
}
