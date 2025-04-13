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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            verticalArrangement = Arrangement.SpaceBetween, // Use SpaceBetween for better distribution
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Top spacing

            GameContent(
                gameState = gameState.value,
                currentStars = currentStars,
                lastThrow = lastThrow,
                throwScoreInput = throwScoreInput,
                onThrowScoreInputChange = {
                    val filteredInput = it.filter { char -> char.isDigit() }
                    throwScoreInput = filteredInput
                    isInputValid =
                        filteredInput.toIntOrNull() != null && filteredInput.toInt() >= 0 && filteredInput.toInt() <= 180
                },
                isInputValid = isInputValid,
                onThrow = {
                    val throwScore = throwScoreInput.toIntOrNull()
                    if (throwScore != null && throwScore >= 0) {
                        gameState.value =
                            updateHighscoreGameState(gameState.value, throwScore)
                        throwScoreInput = ""
                    }
                },
                onUndo = { gameState.value = undoHighscoreGameState(gameState.value) },
                canUndo = canUndo
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 32.dp) // Bottom spacing
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
    throwScoreInput: String,
    onThrowScoreInputChange: (String) -> Unit,
    isInputValid: Boolean,
    onThrow: () -> Unit,
    onUndo: () -> Unit,
    canUndo: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.6f)) // Slightly less transparent
            .padding(vertical = 48.dp, horizontal = 32.dp), // More vertical padding
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp), // Space between elements
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Highscore",
                style = MaterialTheme.typography.headlineMedium, // Larger title
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Score: ${gameState.currentScore}",
                style = MaterialTheme.typography.titleLarge, // Larger score
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Stars: $currentStars",
                style = MaterialTheme.typography.titleLarge, // Larger stars
                color = Color.Yellow, // More emphasis on stars
                fontWeight = FontWeight.Bold
            )
            Text(
                "Throws Remaining: ${gameState.throwsRemaining}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            if (lastThrow != null) {
                Text(
                    "Last Throw: ${lastThrow.score}",
                    style = MaterialTheme.typography.bodyLarge, // More readable
                    color = Color.Yellow,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = throwScoreInput,
                onValueChange = onThrowScoreInputChange,
                label = {
                    Text(
                        "Enter Score (0-180)",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = NumberInputTransformation(),
                isError = !isInputValid,
                modifier = Modifier.fillMaxWidth()
            )
            if (!isInputValid && throwScoreInput.isNotEmpty()) {
                Text(
                    "Invalid score (0-180).",
                    color = androidx.compose.ui.graphics.Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onThrow,
                    enabled = gameState.throwsRemaining > 0 && isInputValid,
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Throw", style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
                Button(
                    onClick = onUndo,
                    enabled = canUndo && gameState.throwsRemaining < 5,
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)) // Blue for Undo
                ) {
                    Text("Undo", style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }
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