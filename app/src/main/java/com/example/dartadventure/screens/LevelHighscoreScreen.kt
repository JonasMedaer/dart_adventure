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
import com.example.dartadventure.utils.StorageHelper
import com.example.dartadventure.data.HighscoreGameState
import com.example.dartadventure.data.calculateStars
import com.example.dartadventure.data.getLevelResult
import com.example.dartadventure.data.updateHighscoreGameState

@Composable
fun LevelHighscoreScreen(navController: NavController) {
    var gameState by remember { mutableStateOf(HighscoreGameState()) }
    var throwScoreInput by remember { mutableStateOf("") }
    var isInputValid by remember { mutableStateOf(true) }
    var currentStars by remember { mutableStateOf(0) }

    // Update stars whenever the score changes
    LaunchedEffect(gameState.currentScore) {
        currentStars = calculateStars(gameState.currentScore)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Highscore")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Score: ${gameState.currentScore}")
        Text("Stars: $currentStars")
        Text("Throws Remaining: ${gameState.throwsRemaining?.div(3) ?: 0}") // Display groups of 3, handle null

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = throwScoreInput,
            onValueChange = {
                throwScoreInput = it
                isInputValid =
                    it.toIntOrNull() != null && it.toInt() >= 0 && it.toInt() <= 180 // Basic validation
            },
            label = { Text("Enter Score for This Throw (3 Darts)") }, // Clarify input
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
                    gameState = updateHighscoreGameState(gameState, throwScore)
                    throwScoreInput = "" // Clear the input field after a valid throw

                    if (gameState.throwsRemaining != null) {
                        if (gameState.throwsRemaining <= 0) {
                            // Game over, save results and navigate back
                            val levelResult = getLevelResult(gameState)
                            StorageHelper.saveLevelResult(levelResult)
                            navController.popBackStack()
                        }
                    }
                }
            },
            enabled = (gameState.throwsRemaining == null || gameState.throwsRemaining > 0) && isInputValid // Handle null
        ) {
            Text("Thrown (3 darts)")
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun LevelHighrcorePreview() {
    val navController = rememberNavController()
    LevelHighscoreScreen(navController = navController)
}