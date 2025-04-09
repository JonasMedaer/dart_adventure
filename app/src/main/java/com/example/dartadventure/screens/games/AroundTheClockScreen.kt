package com.example.dartadventure.screens.games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.data.LevelResult
import com.example.dartadventure.data.aroundtheclock.calculateAroundTheClockScore
import com.example.dartadventure.data.aroundtheclock.updateAroundTheClockGameState
import com.example.dartadventure.data.games.Game
import com.example.dartadventure.data.games.aroundtheclock.AroundTheClockGameState
import com.example.dartadventure.data.games.calculateStars
import com.example.dartadventure.utils.MockStorageHelper
import com.example.dartadventure.utils.StorageHelper
import com.example.dartadventure.utils.StorageInterface

@Composable
fun AroundTheClockScreen(
    navController: NavController,
    gameState: MutableState<AroundTheClockGameState>,
    getGameData: (Int) -> Game?,
    storageHelper: StorageInterface // Ensure you have this parameter
) {
    var dartsThisTurn by remember { mutableStateOf(0) }

    // Use the passed storageHelper here
    val levelResult = storageHelper.getLevelResult(
        gameState.value.currentChapterId,
        gameState.value.currentGameId
    )
    val game = getGameData(gameState.value.currentGameId)
    val starThresholds = game?.starThresholds ?: emptyList()
    var currentStars by remember {
        mutableStateOf(levelResult?.stars ?: 0) // Initialize with levelResult or 0
    }
    // Initialize mustEndOnBullseye once when the screen is composed
    LaunchedEffect(game) { // Key by game
        if (!gameState.value.initialized) {
            val mustEndOnBullseye = game?.mustEndOnBullseye ?: false
            gameState.value = gameState.value.copy(
                mustEndOnBullseye = mustEndOnBullseye,
                initialized = true
            )
        }
        currentStars =
            levelResult?.stars ?: calculateStars(gameState.value.currentScore, starThresholds)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Around the Clock", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Target: ${gameState.value.currentTarget}", style = MaterialTheme.typography.bodyLarge)
        Text(
            "Total Darts Used: ${gameState.value.totalDartsUsed}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text("Current Stars: $currentStars", style = MaterialTheme.typography.bodyLarge)
        Text("Score: ${gameState.value.currentScore}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (dartsThisTurn < 3) {
                        dartsThisTurn++
                        gameState.value = updateAroundTheClockGameState(
                            gameState.value,
                            true
                        )
                        if (gameState.value.gameFinished) {
                            val finalScore =
                                calculateAroundTheClockScore(gameState.value.totalDartsUsed)
                            val currentGame = getGameData(gameState.value.currentGameId)
                            val stars = if (currentGame != null) calculateStars(
                                finalScore,
                                currentGame.starThresholds
                            ) else 0
                            val newLevelResult = LevelResult(
                                chapter = gameState.value.currentChapterId,
                                game = gameState.value.currentGameId,
                                score = finalScore,
                                stars = stars
                            )
                            // Use the passed storageHelper here as well for saving
                            storageHelper.saveLevelResult(newLevelResult)
                            navController.popBackStack()
                        }
                    }
                    if (dartsThisTurn == 3) {
                        dartsThisTurn = 0
                    }
                    currentStars = calculateStars(gameState.value.currentScore, starThresholds)
                },
                enabled = dartsThisTurn < 3,
                modifier = Modifier.width(120.dp)
            ) {
                Text("Hit", style = MaterialTheme.typography.labelLarge)
            }
            Button(
                onClick = {
                    if (dartsThisTurn < 3) {
                        dartsThisTurn++
                        gameState.value = updateAroundTheClockGameState(
                            gameState.value,
                            false
                        )
                    }
                    if (dartsThisTurn == 3) {
                        dartsThisTurn = 0
                    }
                    currentStars = calculateStars(gameState.value.currentScore, starThresholds)
                },
                enabled = dartsThisTurn < 3,
                modifier = Modifier.width(120.dp)
            ) {
                Text("Miss", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun AroundTheClockScreenPreview() {
    val navController = rememberNavController()
    val mockGameState = remember { mutableStateOf(AroundTheClockGameState()) }
    val mockGetGameData: (Int) -> Game? = remember {
        { gameId ->
            Game(
                id = gameId,
                name = "Mock Game",
                description = "This is a mock game for preview.",
                starThresholds = listOf(100, 200, 300, 400, 500),
                initialThrows = null
            )
        }
    }

    MockStorageHelper.initialize()
    val mockStorageHelper: StorageInterface = MockStorageHelper

    AroundTheClockScreen(
        navController = navController,
        gameState = mockGameState,
        getGameData = mockGetGameData,
        storageHelper = mockStorageHelper // Pass the mock instance
    )
}