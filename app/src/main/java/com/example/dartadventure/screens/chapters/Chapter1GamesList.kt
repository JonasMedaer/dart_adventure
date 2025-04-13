// Chapter1GamesList.kt (Refactored)
package com.example.dartadventure.screens

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.R
import com.example.dartadventure.createMockGame
import com.example.dartadventure.data.Chapter
import com.example.dartadventure.data.LevelResult
import com.example.dartadventure.ui.theme.DartAdventureTheme

@Composable
fun Chapter1GamesList(
    navController: NavController,
    chapter: Chapter,
    getLevelResult: (Int, Int) -> LevelResult?
) {
    val backgroundImage = R.drawable.levelselect_path
    val backgroundImageTablet = R.drawable.levelselect_path_tablet

    val gameNavigation: (Int, Int, NavController) -> Unit = { chapterId, gameId, navController ->
        when (gameId) {
            1 -> navController.navigate("highscore_game/$chapterId")
            2 -> navController.navigate("around_the_clock/$chapterId")
        }
    }

    GamesListScreen(
        navController = navController,
        chapter = chapter,
        getLevelResult = getLevelResult,
        backgroundImageId = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE ||
            LocalConfiguration.current.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        ) backgroundImageTablet else backgroundImage,
        gameNavigation = gameNavigation
    )
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun Chapter1GamesListPreview() {
    val navController = rememberNavController()
    val mockChapter = Chapter(
        id = 1,
        name = "The gate",
        description = "First Chapter",
        games = listOf(
            createMockGame(id = 1, name = "Game 1", description = "First Game"),
            createMockGame(id = 2, name = "Game 2", description = "Second Game")
        ),
        requiredStars = 5,
        isUnlocked = true
    )
    val mockLevelResults = mapOf(
        Pair(1, 1) to LevelResult(chapter = 1, game = 1, score = 150, stars = 3),
        Pair(1, 2) to LevelResult(chapter = 1, game = 2, score = 200, stars = 4)
    )
    DartAdventureTheme {
        Chapter1GamesList(
            navController = navController,
            chapter = mockChapter,
            getLevelResult = { chapterId, gameId -> mockLevelResults[Pair(chapterId, gameId)] }
        )
    }
}