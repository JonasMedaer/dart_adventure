package com.example.dartadventure.screens.games

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.R
import com.example.dartadventure.data.games.HighscoreDartThrow
import com.example.dartadventure.data.games.calculateStars
import com.example.dartadventure.data.games.getLevelResult
import com.example.dartadventure.data.games.highscore.HighscoreGameState
import com.example.dartadventure.data.highscore.undoHighscoreGameState
import com.example.dartadventure.data.highscore.updateHighscoreGameState
import com.example.dartadventure.ui.theme.DartAdventureTheme
import com.example.dartadventure.utils.StorageHelper

@Composable
fun HighscoreScreen(
    navController: NavController,
    gameState: MutableState<HighscoreGameState>,
    starThresholds: List<Int>
) {
    var throwScoreInput by remember { mutableStateOf("") }
    var isInputValid by remember { mutableStateOf(true) }
    var currentStars by remember { mutableStateOf(0) }
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val backgroundImage =
        if (isLandscape) R.drawable.highscore_game_tablet else R.drawable.highscore_game
    var showExitDialog by remember { mutableStateOf(false) }
    var lastThrow by remember { mutableStateOf<HighscoreDartThrow?>(null) }
    var canUndo by remember { mutableStateOf(false) }
    var showGameOverDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gameState.value.currentScore) {
        currentStars = calculateStars(gameState.value.currentScore, starThresholds)
    }

    LaunchedEffect(gameState.value.throwsRemaining) {
        if (gameState.value.throwsRemaining <= 0) {
            showGameOverDialog = true
        }
    }

    LaunchedEffect(gameState.value.throws) {
        canUndo = gameState.value.throws.isNotEmpty()
        lastThrow = gameState.value.throws.lastOrNull()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Highscore Background",
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
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Highscore",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )

                    Text(
                        "Score: ${gameState.value.currentScore}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(
                        "Stars: $currentStars",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(
                        "Throws Remaining: ${gameState.value.throwsRemaining}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    if (lastThrow != null) {
                        Text(
                            "Last Throw: ${lastThrow?.score}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Yellow
                        )
                    }

                    OutlinedTextField(
                        value = throwScoreInput,
                        onValueChange = {
                            val filteredInput = it.filter { char -> char.isDigit() }
                            throwScoreInput = filteredInput
                            isInputValid =
                                filteredInput.toIntOrNull() != null && filteredInput.toInt() >= 0 && filteredInput.toInt() <= 180
                        },
                        label = {
                            Text(
                                "Score (3 Darts)",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = NumberInputTransformation(),
                        isError = !isInputValid,
                        modifier = Modifier.width(280.dp)
                    )
                    if (!isInputValid && throwScoreInput.isNotEmpty()) {
                        Text(
                            "Invalid score.",
                            color = androidx.compose.ui.graphics.Color.Red
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                val throwScore = throwScoreInput.toIntOrNull()
                                if (throwScore != null && throwScore >= 0) {
                                    gameState.value =
                                        updateHighscoreGameState(gameState.value, throwScore)
                                    throwScoreInput = ""
                                }
                            },
                            enabled = gameState.value.throwsRemaining > 0 && isInputValid,
                            modifier = Modifier
                                .width(180.dp)
                                .height(60.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text("Throw", style = MaterialTheme.typography.labelLarge)
                        }

                        Button(
                            onClick = {
                                gameState.value = undoHighscoreGameState(gameState.value)
                            },
                            enabled = canUndo && gameState.value.throwsRemaining < 5,
                            modifier = Modifier
                                .width(120.dp)
                                .height(60.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            Text("Undo", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showExitDialog = true }) {
                Text("Back", style = MaterialTheme.typography.labelLarge)
            }
        }
    }

    if (showGameOverDialog) {
        AlertDialog(
            onDismissRequest = { /* Prevent dismissing by tapping outside */ },
            title = { Text("Game end!", style = MaterialTheme.typography.headlineSmall) },
            text = {
                Column {
                    Text(
                        "Current Score: ${gameState.value.currentScore}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Stars Earned: $currentStars",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (lastThrow != null && gameState.value.throws.isNotEmpty()) {
                        Text(
                            "Last Throw: ${lastThrow?.score}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        gameState.value = undoHighscoreGameState(gameState.value)
                        showGameOverDialog = false
                    },
                    enabled = canUndo && gameState.value.throws.isNotEmpty()
                ) {
                    Text("Undo", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showGameOverDialog = false
                        val levelResult = getLevelResult(gameState.value)
                        StorageHelper.saveLevelResult(levelResult)
                        navController.popBackStack()
                    }
                ) {
                    Text("Finish", style = MaterialTheme.typography.labelLarge)
                }
            }
        )
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Confirm Exit", style = MaterialTheme.typography.headlineSmall) },
            text = {
                Text(
                    "Are you sure you want to exit? ",
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

class NumberInputTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val filteredText = text.text.filter { it.isDigit() }
        return TransformedText(
            AnnotatedString(filteredText),
            NumberOffsetMapping(filteredText.length)
        )
    }
}

class NumberOffsetMapping(private val length: Int) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = offset
    override fun transformedToOriginal(offset: Int): Int = offset.coerceAtMost(length)
}


@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun LevelHighscorePreview() {
    val navController = rememberNavController()
    val mockGameState = remember { mutableStateOf(HighscoreGameState(throwsRemaining = 5)) }
    val mockStarThresholds = listOf(100, 150, 200, 250, 300)

    DartAdventureTheme {
        HighscoreScreen(
            navController = navController,
            gameState = mockGameState,
            starThresholds = mockStarThresholds
        )
    }
}