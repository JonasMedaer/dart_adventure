package com.example.dartadventure.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.R
import com.example.dartadventure.chapters
import com.example.dartadventure.data.LevelResult
import com.example.dartadventure.utils.StorageHelper

@Composable
fun Chapter1GameSelectScreen(
    navController: NavController,
    getGameResult: (Int) -> LevelResult? = { gameId -> StorageHelper.getLevelResult(1, gameId) }
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet =
        configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    val backgroundImage = if (isLandscape || isTablet) {
        R.drawable.levelselect_path_tablet
    } else {
        R.drawable.levelselect_path
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Chapter Select Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Chapter: Chapter 1") // Assuming this is always Chapter 1
            Spacer(modifier = Modifier.height(24.dp))

            // ... (rest of your existing content for displaying games) ...
            val chapter = chapters.find { it.id == 1 } ?: return // Get Chapter 1
            chapter.games.forEach { game ->
                val gameResult = getGameResult(game.id)
                val gameHighscore = gameResult?.score ?: 0
                val gameStars = gameResult?.stars ?: 0
                Button(onClick = {
                    when (game.id) {
                        1 -> navController.navigate("highscore_game")
                        2 -> navController.navigate("around_the_clock")
                        // Add navigation for other games in Chapter 1
                    }
                }) {
                    Text(game.name)
                }
                Text("Highscore: $gameHighscore, Stars: $gameStars")
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


private fun getMockGameResult(gameId: Int): LevelResult? {
    // Mock data for preview
    return when (gameId) {
        1 -> LevelResult(chapter = 1, game = 1, score = 150, stars = 3)
        2 -> LevelResult(chapter = 1, game = 2, score = 200, stars = 4)
        else -> null
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun Chapter1GameSelectScreenPreview() {
    val navController = rememberNavController()
    Chapter1GameSelectScreen(
        navController = navController,
        getGameResult = { gameId -> getMockGameResult(gameId) }
    )
}
