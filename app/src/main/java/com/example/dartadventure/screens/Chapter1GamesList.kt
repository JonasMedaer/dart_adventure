// Chapter1GamesList.kt
package com.example.dartadventure.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Chapter: ${chapter.name}", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                    Spacer(modifier = Modifier.height(24.dp))
                    chapter.games.forEach { game ->
                        val gameResult = getLevelResult(chapter.id, game.id)
                        val gameHighscore = gameResult?.score ?: 0
                        val gameStars = gameResult?.stars ?: 0
                        Button(onClick = {
                            when (game.id) {
                                1 -> navController.navigate("highscore_game/${chapter.id}")
                                2 -> navController.navigate("around_the_clock/${chapter.id}")
                            }
                        }) {
                            Text(game.name, style = MaterialTheme.typography.labelLarge)
                        }
                        Text(
                            "Highscore: $gameHighscore, Stars: $gameStars",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            Button(onClick = { navController.popBackStack() }) {
                Text("Back", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun Chapter1GamesListPreview() {
    val navController = rememberNavController()
    val mockChapter = Chapter(
        id = 1,
        name = "The beginning",
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