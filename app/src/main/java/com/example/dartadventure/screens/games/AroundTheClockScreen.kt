package com.example.dartadventure.screens.games

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.R
import com.example.dartadventure.data.LevelResult
import com.example.dartadventure.data.aroundtheclock.calculateAroundTheClockScore
import com.example.dartadventure.data.aroundtheclock.updateAroundTheClockGameState
import com.example.dartadventure.data.games.Game
import com.example.dartadventure.data.games.aroundtheclock.AroundTheClockGameState
import com.example.dartadventure.data.games.calculateStars
import com.example.dartadventure.ui.theme.DartAdventureTheme
import com.example.dartadventure.utils.MockStorageHelper
import com.example.dartadventure.utils.StorageInterface

@Composable
fun AroundTheClockScreen(
    navController: NavController,
    gameState: MutableState<AroundTheClockGameState>,
    getGameData: (Int) -> Game?,
    storageHelper: StorageInterface
) {
    var dartsThisTurn by remember { mutableStateOf(0) }
    val levelResult = storageHelper.getLevelResult(
        gameState.value.currentChapterId,
        gameState.value.currentGameId
    )
    val game = getGameData(gameState.value.currentGameId)
    val starThresholds = game?.starThresholds ?: emptyList()
    var currentStars by remember {
        mutableStateOf(levelResult?.stars ?: 0)
    }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val backgroundImage =
        if (isLandscape) R.drawable.castle_tablet else R.drawable.castle
    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(game) {
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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Around The Clock Background",
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
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Around the Clock",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 64.dp, vertical = 32.dp)

                    ) {
                        Text(
                            "${gameState.value.currentTarget}",
                            style = TextStyle(
                                fontSize = 84.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            ),
                        )
                    }
                    Text(
                        "Total Darts Used: ${gameState.value.totalDartsUsed}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(
                        "Current Stars: $currentStars",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(
                        "Score: ${gameState.value.currentScore}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                        storageHelper.saveLevelResult(newLevelResult)
                                        navController.popBackStack()
                                    }
                                }
                                if (dartsThisTurn == 3) {
                                    dartsThisTurn = 0
                                }
                                currentStars =
                                    calculateStars(gameState.value.currentScore, starThresholds)
                            },
                            enabled = dartsThisTurn < 3,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(0.3f)
                                .fillMaxWidth(0.3f)
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
                                currentStars =
                                    calculateStars(gameState.value.currentScore, starThresholds)
                            },
                            enabled = dartsThisTurn < 3,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(0.3f)
                                .fillMaxWidth(0.3f)
                        ) {
                            Text(
                                "Miss",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
            Button(onClick = { showExitDialog = true }) {
                Text("Back", style = MaterialTheme.typography.labelLarge)
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Confirm Exit", style = MaterialTheme.typography.headlineSmall) },
            text = {
                Text(
                    "Are you sure you want to exit? Your progress will be saved.",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Exit", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                Button(onClick = { showExitDialog = false }) {
                    Text("Cancel", style = MaterialTheme.typography.labelLarge)
                }
            }
        )
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

    DartAdventureTheme {
        AroundTheClockScreen(
            navController = navController,
            gameState = mockGameState,
            getGameData = mockGetGameData,
            storageHelper = mockStorageHelper
        )
    }
}