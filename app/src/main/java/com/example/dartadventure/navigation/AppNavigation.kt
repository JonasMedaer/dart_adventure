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
import com.example.dartadventure.screens.Chapter1GamesList
import com.example.dartadventure.screens.ChapterSelectScreen
import com.example.dartadventure.screens.DartboardBackgroundWithContent
import com.example.dartadventure.screens.SettingsScreen
import com.example.dartadventure.screens.games.AroundTheClockScreen
import com.example.dartadventure.screens.games.HighscoreScreen
import com.example.dartadventure.utils.StorageInterface

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    storageHelper: StorageInterface
) { // Accept storageHelper
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
        composable("game_select/{chapterId}") { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 1
            val chapter = chapters.find { it.id == chapterId } ?: chapters[0]
            val getLevelResult: (Int, Int) -> LevelResult? =
                { chapterId, gameId ->
                    storageHelper.getLevelResult(chapterId, gameId) // Use the passed storageHelper
                }
            when (chapterId) {
                1 -> Chapter1GamesList(navController, chapter, getLevelResult)
                else -> {
                    Text("Invalid Chapter Selected")
                }
            }
        }
        composable("highscore_game/{chapterId}") { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 1
            val chapter = chapters.find { it.id == chapterId }
            val game = chapter?.games?.find { it.id == 1 }

            val initialThrows = game?.initialThrows ?: 5
            val starThresholds = game?.starThresholds ?: listOf(100, 150, 200, 250, 300)

            val gameState = remember {
                mutableStateOf(
                    HighscoreGameState(
                        throwsRemaining = initialThrows,
                        currentChapterId = chapterId
                    )
                )
            }

            HighscoreScreen(
                navController = navController,
                gameState = gameState,
                starThresholds = starThresholds
            )
        }
        composable("around_the_clock/{chapterId}") { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 1
            val chapter = chapters.find { it.id == chapterId }
            val game = chapter?.games?.find { it.id == 2 }

            val gameState = remember {
                mutableStateOf(
                    AroundTheClockGameState(
                        currentChapterId = chapterId,
                        currentGameId = game?.id ?: 2
                    )
                )
            }
            AroundTheClockScreen(
                navController = navController,
                gameState = gameState,
                getGameData = { gameId -> chapters.find { it.games.any { it.id == gameId } }?.games?.find { it.id == gameId } },
                storageHelper = storageHelper // Pass the storageHelper here as well
            )
        }
    }
}