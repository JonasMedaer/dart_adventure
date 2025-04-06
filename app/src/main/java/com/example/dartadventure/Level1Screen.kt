package com.example.dartadventure

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Level1Screen(navController: NavController) {
    var gameState by remember { mutableStateOf(GameState()) }
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
        Text("Level 1: Highscore")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Score: ${gameState.currentScore}")
        Text("Stars: $currentStars")
        Text("Throws Remaining: ${gameState.throwsRemaining}")

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = throwScoreInput,
            onValueChange = {
                throwScoreInput = it
                isInputValid =
                    it.toIntOrNull() != null && it.toInt() >= 0 && it.toInt() <= 180 // Basic validation
            },
            label = { Text("Enter Score for This Throw") },
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
                    gameState = updateGameState(gameState, throwScore)
                    throwScoreInput = "" // Clear the input field after a valid throw

                    if (gameState.throwsRemaining <= 0) {
                        // Game over, save results and navigate back
                        val levelResult = getLevelResult(gameState)
                        StorageHelper.saveLevelResult(levelResult)
                        navController.popBackStack()
                    }
                }
            },
            enabled = gameState.throwsRemaining > 0 && isInputValid
        ) {
            Text("Thrown (3 darts)")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Level1ScreenPreview() {
    val navController = rememberNavController()
    Level1Screen(navController = navController)
}