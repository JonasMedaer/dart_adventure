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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import com.example.dartadventure.ui.components.NumpadComponent
import com.example.dartadventure.ui.theme.DartAdventureTheme
import com.example.dartadventure.utils.StorageHelper

@Composable
fun HighscoreScreen(
    navController: NavController,
    gameState: MutableState<HighscoreGameState>,
    starThresholds: List<Int>
) {
    var enteredScore by remember { mutableStateOf("") }
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)) // Semi-transparent background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                GameContent(
                    gameState = gameState.value,
                    currentStars = currentStars,
                    lastThrow = lastThrow,
                    enteredScore = enteredScore,
                    onNumpadClick = { digit ->
                        if (enteredScore.length < 3) {
                            enteredScore += digit
                        }
                    },
                    onClearClick = { enteredScore = "" },
                    isInputValid = isInputValid,
                    onThrow = {
                        val score = enteredScore.toIntOrNull()
                        if (score != null && score in 0..180) {
                            gameState.value = updateHighscoreGameState(gameState.value, score)
                            enteredScore = ""
                            isInputValid = true // Reset here as well for immediate feedback
                        } else if (enteredScore.isNotEmpty()) {
                            isInputValid = false // Set here as well
                        }
                    },
                    onUndo = { gameState.value = undoHighscoreGameState(gameState.value) },
                    canUndo = canUndo,
                    isLandscape = isLandscape,
                    onIsInputValidChange = { newValue -> isInputValid = newValue }
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Button(
                        onClick = { showExitDialog = true },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(60.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Back", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }

    GameOverDialog(
        showDialog = showGameOverDialog,
        gameState = gameState.value,
        currentStars = currentStars,
        lastThrow = lastThrow,
        canUndo = canUndo,
        onUndo = { gameState.value = undoHighscoreGameState(gameState.value) },
        onDismiss = { showGameOverDialog = false },
        onFinish = {
            showGameOverDialog = false
            val levelResult = getLevelResult(gameState.value)
            StorageHelper.saveLevelResult(levelResult)
            navController.popBackStack()
        }
    )

    ExitConfirmationDialog(
        showDialog = showExitDialog,
        onDismiss = { showExitDialog = false },
        onConfirmExit = { navController.popBackStack() }
    )
}

@Composable
private fun GameContent(
    gameState: HighscoreGameState,
    currentStars: Int,
    lastThrow: HighscoreDartThrow?,
    enteredScore: String,
    onNumpadClick: (String) -> Unit,
    onClearClick: () -> Unit,
    isInputValid: Boolean,
    onThrow: () -> Unit,
    onUndo: () -> Unit,
    canUndo: Boolean,
    isLandscape: Boolean,
    onIsInputValidChange: (Boolean) -> Unit
) {
    val localDensity = LocalDensity.current
    val lastThrowHeightDp =
        with(localDensity) { MaterialTheme.typography.bodyLarge.lineHeight.toDp() }

    if (isLandscape) {
        // Landscape Layout (Two Columns)
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f)
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column: Game Information
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp), // Increased vertical space
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Highscore",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Score: ${gameState.currentScore}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Stars: $currentStars",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Yellow,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Throws Remaining: ${gameState.throwsRemaining}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                // Ensure enough space for "Last Throw"
                Box(modifier = Modifier.height(lastThrowHeightDp * 2)) { // Increased height
                    if (lastThrow != null) {
                        Text(
                            "Last Throw: ${lastThrow.score}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Yellow,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.Center) // Center vertically in the box
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(32.dp)) // Add some space between columns

            // Right Column: Score Input and Numpad
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Score Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (enteredScore.isEmpty()) "Enter Score" else enteredScore,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (enteredScore.isEmpty()) Color.Gray else Color.White
                    )
                }
                if (!isInputValid && enteredScore.isNotEmpty()) {
                    Text(
                        "Invalid score (0-180).",
                        color = androidx.compose.ui.graphics.Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                NumpadComponent(
                    onNumpadClick = onNumpadClick,
                    onClearClick = onClearClick,
                    buttonSize = 60.dp, // Adjust as needed
                    spacing = 8.dp,    // Adjust as needed
                    isLandscape = true // Ensure Numpad uses landscape layout
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val score = enteredScore.toIntOrNull()
                            if (score != null && score in 0..180) {
                                onThrow() // Call the original onThrow
                                onIsInputValidChange(true) // Update the state in the parent
                            } else if (enteredScore.isNotEmpty()) {
                                onIsInputValidChange(false) // Update the state in the parent
                            }
                        },
                        enabled = gameState.throwsRemaining > 0 && enteredScore.isNotEmpty(),
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp), // Adjust height as needed
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            "Throw",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                    Button(
                        onClick = onUndo,
                        enabled = canUndo && gameState.throwsRemaining < 5,
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp), // Adjust height as needed
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
                    ) {
                        Text(
                            "Undo",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    } else {
        // Portrait Layout (Existing Column Structure)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.85f)
                .padding(vertical = 24.dp, horizontal = 32.dp)
        ) {
            Text(
                "Highscore",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Score: ${gameState.currentScore}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Stars: $currentStars",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Yellow,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Throws Remaining: ${gameState.throwsRemaining}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            // Ensure enough space for "Last Throw"
            Box(modifier = Modifier.height(lastThrowHeightDp * 2)) { // Increased height
                if (lastThrow != null) {
                    Text(
                        "Last Throw: ${lastThrow.score}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Yellow,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Center) // Center vertically
                    )
                }
            }

            // Score Display
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(72.dp)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (enteredScore.isEmpty()) "Enter Score" else enteredScore,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (enteredScore.isEmpty()) Color.Gray else Color.White
                )
            }
            if (!isInputValid && enteredScore.isNotEmpty()) {
                Text(
                    "Invalid score (0-180).",
                    color = androidx.compose.ui.graphics.Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            NumpadComponent(
                onNumpadClick = onNumpadClick,
                onClearClick = onClearClick,
                buttonSize = 50.dp,
                spacing = 6.dp,
                isLandscape = false // Ensure Numpad uses portrait layout
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val score = enteredScore.toIntOrNull()
                        if (score != null && score in 0..180) {
                            onThrow() // Call the original onThrow
                            onIsInputValidChange(true) // Update the state in the parent
                        } else if (enteredScore.isNotEmpty()) {
                            onIsInputValidChange(false) // Update the state in the parent
                        }
                    },
                    enabled = gameState.throwsRemaining > 0 && enteredScore.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        "Throw",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
                Button(
                    onClick = onUndo,
                    enabled = canUndo && gameState.throwsRemaining < 5,
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
                ) {
                    Text(
                        "Undo",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun NumpadButton(
    text: String,
    onClick
    : (String) -> Unit,
    size: Dp = 60.dp,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall
) {
    Button(
        onClick = { onClick(text) },
        modifier = Modifier
            .width(size)
            .height(size),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text, style = textStyle)
        }
    }
}


@Composable
private fun GameOverDialog(
    showDialog: Boolean,
    gameState: HighscoreGameState,
    currentStars: Int,
    lastThrow: HighscoreDartThrow?,
    canUndo: Boolean,
    onUndo: () -> Unit,
    onDismiss: () -> Unit,
    onFinish: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Game Over!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Final Score: ${gameState.currentScore}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Stars Earned: $currentStars",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Yellow,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (lastThrow != null && gameState.throws.isNotEmpty()) {
                        Text(
                            "Last Throw: ${lastThrow.score}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Yellow
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onFinish,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Finish", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                if (canUndo && gameState.throws.isNotEmpty()) {
                    Button(
                        onClick = onUndo,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Undo Last", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        )
    }
}

@Composable
private fun ExitConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmExit: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Confirm Exit",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to exit? ",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirmExit,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Exit", style = MaterialTheme.typography.labelLarge)
                }
            },
            dismissButton = {
                Button(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
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