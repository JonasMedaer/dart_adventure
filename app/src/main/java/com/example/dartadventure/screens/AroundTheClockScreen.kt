package com.example.dartadventure.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.dartadventure.data.AroundTheClockDartThrow
import com.example.dartadventure.data.AroundTheClockGameState
import com.example.dartadventure.data.calculateAroundTheClockScore

@Composable
fun AroundTheClockScreen(
    navController: NavController,
    gameState: MutableState<AroundTheClockGameState>
) {
    // Remove the local gameState variable
    // var gameState by remember { gameState } // This is the problem!
    var dartsThisTurn by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Around the Clock")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Target: ${gameState.value.currentTarget}")
        Text("Total Darts Used: ${gameState.value.totalDartsUsed}")
        Text("Score: ${gameState.value.currentScore}")

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    if (dartsThisTurn < 3) {
                        dartsThisTurn++
                        gameState.value = gameState.value.copy(
                            totalDartsUsed = gameState.value.totalDartsUsed + 1,
                            currentScore = calculateAroundTheClockScore(gameState.value.totalDartsUsed + 1),
                            currentTarget = if (gameState.value.currentTarget == 20) 25 else gameState.value.currentTarget + 1,
                            throws = gameState.value.throws.toMutableList().apply {
                                add(AroundTheClockDartThrow(gameState.value.currentTarget, true))
                            }
                        )
                        if (gameState.value.currentTarget == 25) {
                            navController.popBackStack()
                        }
                    }
                    if (dartsThisTurn == 3) {
                        dartsThisTurn = 0
                    }
                },
                enabled = dartsThisTurn < 3
            ) {
                Text("Hit")
            }

            Button(
                onClick = {
                    if (dartsThisTurn < 3) {
                        dartsThisTurn++
                        gameState.value = gameState.value.copy( // Update using gameState.value
                            totalDartsUsed = gameState.value.totalDartsUsed + 1,
                            currentScore = calculateAroundTheClockScore(gameState.value.totalDartsUsed + 1),
                            throws = gameState.value.throws.toMutableList()
                                .apply {
                                    add(
                                        AroundTheClockDartThrow(
                                            gameState.value.currentTarget,
                                            false
                                        )
                                    )
                                }
                        )
                    }
                    if (dartsThisTurn == 3) {
                        dartsThisTurn = 0
                    }
                },
                enabled = dartsThisTurn < 3
            ) {
                Text("Miss")
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
    val mockGameState =
        remember { mutableStateOf(AroundTheClockGameState()) } // Provide initial gameState
    AroundTheClockScreen(navController = navController, gameState = mockGameState)
}