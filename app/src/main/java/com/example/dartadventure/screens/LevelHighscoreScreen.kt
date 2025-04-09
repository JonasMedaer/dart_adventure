package com.example.dartadventure.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.data.games.calculateStars
import com.example.dartadventure.data.games.getLevelResult
import com.example.dartadventure.data.games.highscore.HighscoreGameState
import com.example.dartadventure.data.highscore.updateHighscoreGameState
import com.example.dartadventure.utils.StorageHelper

@Composable
fun LevelHighscoreScreen(
    navController: NavController,
    gameState: MutableState<HighscoreGameState>,
    starThresholds: List<Int> // Add this parameter
) {
    var throwScoreInput by remember { mutableStateOf("") }
    var isInputValid by remember { mutableStateOf(true) }
    var currentStars by remember { mutableStateOf(0) }

    // Update stars whenever the score changes
    LaunchedEffect(gameState.value.currentScore) {
        currentStars =
            calculateStars(gameState.value.currentScore, starThresholds) // Pass thresholds
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Highscore")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Score: ${gameState.value.currentScore}")
        Text("Stars: $currentStars")
        Text("Throws Remaining: ${gameState.value.throwsRemaining}")

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = throwScoreInput,
            onValueChange = {
                throwScoreInput = it
                isInputValid =
                    it.toIntOrNull() != null && it.toInt() >= 0 && it.toInt() <= 180
            },
            label = { Text("Enter Score for This Throw (3 Darts)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = !isInputValid,
            modifier = Modifier.width(280.dp)
        )
        if (!isInputValid && throwScoreInput.isNotEmpty()) {
            Text(
                "Invalid score.",
                color = androidx.compose.ui.graphics.Color.Red
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val throwScore = throwScoreInput.toIntOrNull()
                if (throwScore != null && throwScore >= 0) {
                    gameState.value = updateHighscoreGameState(gameState.value, throwScore)
                    throwScoreInput = ""

                    if (gameState.value.throwsRemaining <= 0) {
                        val levelResult = getLevelResult(gameState.value)
                        StorageHelper.saveLevelResult(levelResult)
                        navController.popBackStack()
                    }
                }
            },
            enabled = gameState.value.throwsRemaining > 0 && isInputValid
        ) {
            Text("Thrown (3 darts)")
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun LevelHighscorePreview() {
    val navController = rememberNavController()
    val mockGameState = remember { mutableStateOf(HighscoreGameState(throwsRemaining = 5)) }
    val mockStarThresholds = listOf(100, 150, 200, 250, 300) // Provide mock thresholds

    LevelHighscoreScreen(
        navController = navController,
        gameState = mockGameState,
        starThresholds = mockStarThresholds // Pass mock thresholds
    )
}