// navigation/AppNavigation.kt
package com.example.dartadventure.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.chapters
import com.example.dartadventure.data.HighscoreGameState
import com.example.dartadventure.data.LevelResult
import com.example.dartadventure.screens.Chapter1GamesList
import com.example.dartadventure.screens.ChapterSelectScreen
import com.example.dartadventure.screens.DartboardBackgroundWithContent
import com.example.dartadventure.screens.LevelHighscoreScreen
import com.example.dartadventure.screens.SettingsScreen
import com.example.dartadventure.utils.StorageHelper

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            DartboardBackgroundWithContent(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
        composable("chapter_select") {
            ChapterSelectScreen(navController = navController)
        }
        composable("game_select/{chapterId}") { backStackEntry -> // Use parameterized route
            val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 1
            val chapter = chapters.find { it.id == chapterId } ?: chapters[0]
            val getLevelResult: (Int) -> LevelResult? = { gameId ->
                StorageHelper.getLevelResult(chapterId, gameId)
            }
            when (chapterId) {
                1 -> Chapter1GamesList(navController, chapter, getLevelResult)
                // Add more cases for other chapters as needed
                else -> {
                    // Handle the case where chapterId doesn't match any known chapter
                    // For example, you could navigate back, show an error, or display a default screen
                    Text("Invalid Chapter Selected") // Placeholder
                }
            }
        }
        composable("highscore_game/{chapterId}") { backStackEntry -> // Parameterized route
            val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 1
            val initialThrows = if (chapterId == 2) 10 else 5 // Determine throws based on chapter
            val gameState = remember {
                mutableStateOf(
                    HighscoreGameState(
                        throwsRemaining = initialThrows,
                        currentChapterId = chapterId
                    )
                )
            }
            LevelHighscoreScreen(navController = navController, gameState = gameState)
        }
        composable("around_the_clock") {
            AroundTheClockScreen(navController = navController) // Replace with your actual composable
        }
    }
}

@Composable
fun AroundTheClockScreen(navController: NavController) {
    // Placeholder composable - no content yet
}

