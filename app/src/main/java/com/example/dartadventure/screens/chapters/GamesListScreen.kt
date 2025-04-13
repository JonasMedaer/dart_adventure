// GamesListScreen.kt (New composable for reusability)
package com.example.dartadventure.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dartadventure.data.Chapter
import com.example.dartadventure.data.LevelResult

@Composable
fun GamesListScreen(
    navController: NavController,
    chapter: Chapter,
    getLevelResult: (Int, Int) -> LevelResult?,
    backgroundImageId: Int,
    gameNavigation: (Int, Int, NavController) -> Unit // Lambda for game navigation
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImageId),
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
                    Text(
                        "${chapter.name}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    chapter.games.forEach { game ->
                        val gameResult = getLevelResult(chapter.id, game.id)
                        val gameHighscore = gameResult?.score ?: 0
                        val gameStars = gameResult?.stars ?: 0
                        Button(onClick = { gameNavigation(chapter.id, game.id, navController) }) {
                            Text(game.name, style = MaterialTheme.typography.labelLarge)
                        }
                        Text(
                            "Highscore: $gameHighscore, Stars: $gameStars / 5",
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