// Chapter2GamesList.kt (Refactored)
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
fun Chapter2GamesList(
    navController: NavController,
    chapter: Chapter,
    getLevelResult: (Int, Int) -> LevelResult?
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val backgroundImage =
        if (isLandscape) R.drawable.harbor_tablet else R.drawable.harbor

    val gameNavigation: (Int, Int, NavController) -> Unit = { chapterId, gameId, navController ->
        when (gameId) {
            3 -> navController.navigate("target_practice/$chapterId")
            4 -> navController.navigate("elimination/$chapterId")
        }
    }

    GamesListScreen(
        navController = navController,
        chapter = chapter,
        getLevelResult = getLevelResult,
        backgroundImageId = backgroundImage,
        gameNavigation = gameNavigation
    )
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun Chapter2GamesListPreview() {
    val navController = rememberNavController()
    val mockChapter = Chapter(
        id = 2,
        name = "The harbor",
        description = "Second Chapter",
        games = listOf(
            createMockGame(id = 3, name = "Target Practice", description = "New Game 1"),
            createMockGame(id = 4, name = "Elimination", description = "New Game 2")
        ),
        requiredStars = 10,
        isUnlocked = false
    )
    val mockLevelResults = mapOf(
        Pair(2, 3) to LevelResult(chapter = 2, game = 3, score = 180, stars = 4),
        Pair(2, 4) to LevelResult(chapter = 2, game = 4, score = 250, stars = 5)
    )
    DartAdventureTheme {
        Chapter2GamesList(
            navController = navController,
            chapter = mockChapter,
            getLevelResult = { chapterId, gameId -> mockLevelResults[Pair(chapterId, gameId)] }
        )
    }
}