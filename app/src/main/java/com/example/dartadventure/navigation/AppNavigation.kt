// navigation/AppNavigation.kt
package com.example.dartadventure.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.screens.Chapter1GameSelectScreen
import com.example.dartadventure.screens.ChapterSelectScreen
import com.example.dartadventure.screens.DartboardBackgroundWithContent
import com.example.dartadventure.screens.GameSelectScreen
import com.example.dartadventure.screens.LevelHighscoreScreen
import com.example.dartadventure.screens.SettingsScreen

@Composable
fun AppNavigation(innerPadding: PaddingValues, modifier: Modifier = Modifier) {
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
        composable("chapter1_games") {
            Chapter1GameSelectScreen(navController)
        }
        composable("game_select/{chapterId}") { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId")?.toIntOrNull() ?: 1
            GameSelectScreen(navController = navController, chapterId = chapterId)
        }
        composable("highscore_game") {
            LevelHighscoreScreen(navController = navController)
        }
    }
}