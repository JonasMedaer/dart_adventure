// navigation/AppNavigation.kt
package com.example.dartadventure.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.chapters
import com.example.dartadventure.data.LevelResult
import com.example.dartadventure.data.games.aroundtheclock.AroundTheClockGameState
import com.example.dartadventure.data.games.highscore.HighscoreGameState
import com.example.dartadventure.screens.AroundTheClockScreen
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
            val getLevelResult: (Int, Int) -> LevelResult? =
                { chapterId, gameId -> // Modified lambda
                    StorageHelper.getLevelResult(chapterId, gameId) // Call with both parameters
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

            // Retrieve game data
            val chapter = chapters.find { it.id == chapterId }
            val game = chapter?.games?.find { it.id == 1 } // Assuming Highscore game ID is 1

            // Get initial throws and star thresholds from game data
            val initialThrows =
                game?.initialThrows ?: 5 // Provide a default if not found (or handle the error)
            val starThresholds = game?.starThresholds ?: listOf(
                100,
                150,
                200,
                250,
                300
            ) // Provide a default if not found (or handle the error)

            val gameState = remember {
                mutableStateOf(
                    HighscoreGameState(
                        throwsRemaining = initialThrows,
                        currentChapterId = chapterId
                    )
                )
            }

            LevelHighscoreScreen(
                navController = navController,
                gameState = gameState,
                starThresholds = starThresholds
            )
        }
        composable("around_the_clock/{chapterId}") { backStackEntry -> // Parameterized route
            val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 1

            // Retrieve game data for "Around the Clock" (gameId = 2)
            val chapter = chapters.find { it.id == chapterId }
            val game = chapter?.games?.find { it.id == 2 } // Get game data for Around the Clock

            val gameState = remember {
                mutableStateOf(
                    AroundTheClockGameState(
                        currentChapterId = chapterId,
                        currentGameId = game?.id
                            ?: 2 // Initialize currentGameId with 2 (or a default)
                    )
                )
            }
            AroundTheClockScreen(
                navController = navController,
                gameState = gameState,
                getGameData = { gameId -> chapters.find { it.games.any { it.id == gameId } }?.games?.find { it.id == gameId } }
            )
        }
    }
}
