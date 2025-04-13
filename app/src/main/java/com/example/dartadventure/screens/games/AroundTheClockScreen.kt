package com.example.dartadventure.screens.games

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.R
import com.example.dartadventure.data.LevelResult
import com.example.dartadventure.data.aroundtheclock.calculateAroundTheClockScore
import com.example.dartadventure.data.aroundtheclock.undoAroundTheClockGameState
import com.example.dartadventure.data.aroundtheclock.undoLastThrowAfterGameOver
import com.example.dartadventure.data.aroundtheclock.updateAroundTheClockGameState
import com.example.dartadventure.data.games.AroundTheClockDartThrow
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
    var dartsThisTurn by remember { mutableStateOf(gameState.value.dartsThrown % 3) }
    var canUndo by remember { mutableStateOf(gameState.value.throws.isNotEmpty()) } // Initialize based on initial state
    var showGameOverDialog by remember { mutableStateOf(false) }
    val levelResult = storageHelper.getLevelResult(
        gameState.value.currentChapterId,
        gameState.value.currentGameId
    )
    val game = getGameData(gameState.value.currentGameId)
    val starThresholds = game?.starThresholds ?: emptyList()
    var currentStars by remember { mutableStateOf(0) } // Initialize to 0, LaunchedEffect will update
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val backgroundImage =
        if (isLandscape) R.drawable.castle_tablet else R.drawable.castle
    var showExitDialog by remember { mutableStateOf(false) }
    var lastThrow by remember { mutableStateOf<AroundTheClockDartThrow?>(null) }

    // Fetch initial game data like mustEndOnBullseye
    LaunchedEffect(game) {
        if (game != null && !gameState.value.initialized) {
            val mustEndOnBullseye = game.mustEndOnBullseye ?: false
            gameState.value = gameState.value.copy(
                mustEndOnBullseye = mustEndOnBullseye,
                initialized = true
            )
        }
    }

    // Calculate initial stars and score based on potentially loaded state
    LaunchedEffect(Unit, gameState.value.totalDartsUsed, starThresholds) {
        // Recalculate score and stars when darts used changes or thresholds load
        // This ensures consistency if gameState is loaded externally
        val score = calculateAroundTheClockScore(gameState.value.totalDartsUsed)
        gameState.value = gameState.value.copy(currentScore = score) // Update score in state
        currentStars = calculateStars(score, starThresholds)
        // Update canUndo based on loaded throws
        canUndo = gameState.value.throws.isNotEmpty()
        lastThrow = gameState.value.throws.lastOrNull()
        // Reset darts this turn based on loaded state
        dartsThisTurn = gameState.value.dartsThrown % 3
    }

    // Observe game state changes for stars, game over, undo state, last throw
    LaunchedEffect(gameState.value) {
        currentStars = calculateStars(gameState.value.currentScore, starThresholds)
        if (gameState.value.gameFinished && !showGameOverDialog) {
            showGameOverDialog = true
        }
        canUndo = gameState.value.throws.isNotEmpty()
        lastThrow = gameState.value.throws.lastOrNull()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Around The Clock Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)) // Semi-transparent background
        ) {
            // Choose layout based on orientation
            if (isLandscape) {
                LandscapeLayout(
                    gameState = gameState,
                    currentStars = currentStars,
                    lastThrow = lastThrow,
                    dartsThisTurn = dartsThisTurn,
                    onDartsThisTurnChange = { dartsThisTurn = it },
                    starThresholds = starThresholds,
                    onCanUndoChange = { canUndo = it },
                    canUndo = canUndo,
                    onUndo = {
                        gameState.value = undoAroundTheClockGameState(gameState.value)
                        dartsThisTurn = gameState.value.dartsThrown % 3
                    },
                    onBack = { showExitDialog = true }
                )
            } else {
                PortraitLayout(
                    gameState = gameState,
                    currentStars = currentStars,
                    lastThrow = lastThrow,
                    dartsThisTurn = dartsThisTurn,
                    onDartsThisTurnChange = { dartsThisTurn = it },
                    starThresholds = starThresholds,
                    onCanUndoChange = { canUndo = it },
                    canUndo = canUndo,
                    onUndo = {
                        gameState.value = undoAroundTheClockGameState(gameState.value)
                        dartsThisTurn = gameState.value.dartsThrown % 3
                    },
                    onBack = { showExitDialog = true }
                )
            }
        }
    }

    // Dialogs remain the same
    GameOverDialog(
        showDialog = showGameOverDialog,
        gameState = gameState.value,
        starThresholds = starThresholds,
        storageHelper = storageHelper,
        onDismiss = { showGameOverDialog = false },
        onFinish = {
            val finalScore = calculateAroundTheClockScore(gameState.value.totalDartsUsed)
            val stars = calculateStars(finalScore, starThresholds)
            val newLevelResult = LevelResult(
                chapter = gameState.value.currentChapterId,
                game = gameState.value.currentGameId,
                score = finalScore,
                stars = stars
            )
            storageHelper.saveLevelResult(newLevelResult)
            navController.popBackStack()
        },
        onUndoLast = {
            gameState.value = undoLastThrowAfterGameOver(gameState.value)
            dartsThisTurn = gameState.value.dartsThrown % 3
            showGameOverDialog = false
        },
        canUndo = canUndo
    )

    ExitConfirmationDialog(
        showDialog = showExitDialog,
        onDismiss = { showExitDialog = false },
        onConfirmExit = { navController.popBackStack() }
    )
}

@Composable
private fun PortraitLayout(
    gameState: MutableState<AroundTheClockGameState>,
    currentStars: Int,
    lastThrow: AroundTheClockDartThrow?,
    dartsThisTurn: Int,
    onDartsThisTurnChange: (Int) -> Unit,
    starThresholds: List<Int>,
    onCanUndoChange: (Boolean) -> Unit,
    canUndo: Boolean,
    onUndo: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopSection(
            gameState = gameState.value,
            currentStars = currentStars,
            lastThrow = lastThrow,
            isLandscape = false
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ActionButtons(
                gameState = gameState,
                dartsThisTurn = dartsThisTurn,
                onDartsThisTurnChange = onDartsThisTurnChange,
                starThresholds = starThresholds,
                onCanUndoChange = onCanUndoChange,
                isLandscape = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            UndoButtonComposable(
                onUndo = onUndo,
                canUndo = canUndo
            )

            Spacer(modifier = Modifier.height(16.dp))

            BackButtonComposable(
                onBack = onBack
            )
        }
    }
}

@Composable
private fun LandscapeLayout(
    gameState: MutableState<AroundTheClockGameState>,
    currentStars: Int,
    lastThrow: AroundTheClockDartThrow?,
    dartsThisTurn: Int,
    onDartsThisTurnChange: (Int) -> Unit,
    starThresholds: List<Int>,
    onCanUndoChange: (Boolean) -> Unit,
    canUndo: Boolean,
    onUndo: () -> Unit,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            TopSection(
                gameState = gameState.value,
                currentStars = currentStars,
                lastThrow = lastThrow,
                isLandscape = true
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // Centered Controls (Hit, Miss, Undo)
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActionButtons(
                    gameState = gameState,
                    dartsThisTurn = dartsThisTurn,
                    onDartsThisTurnChange = onDartsThisTurnChange,
                    starThresholds = starThresholds,
                    onCanUndoChange = onCanUndoChange,
                    isLandscape = true
                )
                Spacer(modifier = Modifier.height(24.dp)) // Space between Action and Undo
                UndoButtonComposable(
                    onUndo = onUndo,
                    canUndo = canUndo
                )
            }

            BackButtonComposable(
                onBack = onBack,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun TopSection(
    gameState: AroundTheClockGameState,
    currentStars: Int,
    lastThrow: AroundTheClockDartThrow?,
    isLandscape: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        // Reduce top padding slightly in landscape to center better
        modifier = Modifier.padding(top = if (isLandscape) 16.dp else 32.dp)
    ) {
        Text(
            "Around the Clock",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Slightly smaller target display in landscape? Optional.
        val boxSize = if (isLandscape) 140.dp else 160.dp
        val paddingSize = if (isLandscape) 40.dp else 48.dp
        val fontSize = if (isLandscape) 100.sp else 120.sp

        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    CircleShape
                )
                .padding(paddingSize)
                .width(boxSize)
                .height(boxSize),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${gameState.currentTarget}",
                style = TextStyle(
                    fontSize = fontSize,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Clip
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Darts Used: ${gameState.totalDartsUsed}",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        val maxStars = 5
        Text(
            "Stars: $currentStars / $maxStars",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Text(
            "Score: ${gameState.currentScore}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        val lineHeightDp =
            with(LocalDensity.current) { MaterialTheme.typography.bodyLarge.lineHeight.toDp() }
        // Use minHeight instead of fixed height to allow shrinking if no text
        Box(modifier = Modifier.defaultMinSize(minHeight = lineHeightDp * 2)) {
            if (lastThrow != null) {
                Text(
                    // Display slightly shorter text in landscape if needed
                    text = "Last: ${if (lastThrow.hit) "Hit" else "Miss"} on ${lastThrow.target}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Yellow,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    gameState: MutableState<AroundTheClockGameState>,
    dartsThisTurn: Int,
    onDartsThisTurnChange: (Int) -> Unit,
    starThresholds: List<Int>,
    onCanUndoChange: (Boolean) -> Unit,
    isLandscape: Boolean
) {
    // Use Column in Landscape, Row in Portrait
    val arrangement =
        if (isLandscape) Arrangement.spacedBy(16.dp) else Arrangement.spacedBy(16.dp) // Can adjust spacing differently
    val modifier = Modifier
        .fillMaxWidth(if (isLandscape) 0.9f else 1f) // Take less width in landscape column
        .padding(horizontal = if (isLandscape) 0.dp else 32.dp) // No horizontal padding needed if column manages width

    if (isLandscape) {
        // Buttons stacked vertically in landscape's right column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = arrangement, // Vertical spacing
            modifier = modifier
        ) {
            HitMissButton(
                gameState,
                true,
                dartsThisTurn,
                onDartsThisTurnChange,
                onCanUndoChange,
                Modifier.fillMaxWidth()
            )
            HitMissButton(
                gameState,
                false,
                dartsThisTurn,
                onDartsThisTurnChange,
                onCanUndoChange,
                Modifier.fillMaxWidth()
            )
        }
    } else {
        // Buttons side-by-side in portrait
        Row(
            horizontalArrangement = arrangement, // Horizontal spacing
            modifier = modifier
        ) {
            HitMissButton(
                gameState,
                true,
                dartsThisTurn,
                onDartsThisTurnChange,
                onCanUndoChange,
                Modifier.weight(1f)
            )
            HitMissButton(
                gameState,
                false,
                dartsThisTurn,
                onDartsThisTurnChange,
                onCanUndoChange,
                Modifier.weight(1f)
            )
        }
    }
    // Removed Spacer here, handled in parent layouts
}

// Extracted common button logic
@Composable
private fun HitMissButton(
    gameState: MutableState<AroundTheClockGameState>,
    isHitButton: Boolean,
    dartsThisTurn: Int,
    onDartsThisTurnChange: (Int) -> Unit,
    onCanUndoChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier // Pass modifier
) {
    val text = if (isHitButton) "Hit" else "Miss"
    val color = if (isHitButton) Color(0xFF4CAF50) else Color(0xFFF44336)
    Button(
        onClick = {
            if (dartsThisTurn < 3 && !gameState.value.gameFinished) {
                gameState.value = updateAroundTheClockGameState(
                    gameState.value,
                    isHitButton // Pass true for Hit, false for Miss
                )
                val nextDartsThisTurn = (dartsThisTurn + 1) % 3 // Cycle 0, 1, 2, 0...
                onDartsThisTurnChange(nextDartsThisTurn)
                onCanUndoChange(true) // Enable undo after any action
            }
            // Removed the separate dartsThisTurn reset logic, handled by modulo now
        },
        enabled = !gameState.value.gameFinished,
        modifier = modifier.height(72.dp), // Keep consistent height
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, color = Color.White)
    }
}


@Composable
private fun UndoButtonComposable(
    onUndo: () -> Unit,
    canUndo: Boolean,
    modifier: Modifier = Modifier // Allow passing modifier if needed for specific cases
) {
    Button(
        onClick = onUndo,
        enabled = canUndo,
        modifier = modifier // Apply modifier passed, if any
            .fillMaxWidth(0.9f) // Match width from previous GameControlButtons landscape style
            .height(72.dp), // Standard height
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6)) // Specific Undo color
    ) {
        Text("Undo", style = MaterialTheme.typography.titleMedium, color = Color.White)
    }
}

@Composable
private fun BackButtonComposable(
    onBack: () -> Unit,
    modifier: Modifier = Modifier // Modifier now primarily used for alignment
) {
    Button(
        onClick = onBack,
        modifier = modifier // Apply modifier (includes alignment from caller)
            .fillMaxWidth(0.9f) // Match width from previous GameControlButtons landscape style
            .height(60.dp), // Standard height
        shape = RoundedCornerShape(12.dp)
        // Default button colors
    ) {
        Text("Back", style = MaterialTheme.typography.titleMedium)
    }
}


@Composable
private fun GameOverDialog(
    showDialog: Boolean,
    gameState: AroundTheClockGameState,
    starThresholds: List<Int>,
    storageHelper: StorageInterface,
    onDismiss: () -> Unit,
    onFinish: () -> Unit,
    onUndoLast: () -> Unit,
    canUndo: Boolean
) {
    if (showDialog) {
        val finalScore = calculateAroundTheClockScore(gameState.totalDartsUsed)
        val finalStars = calculateStars(finalScore, starThresholds)
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
                Column {
                    Text(
                        "Total Darts: ${gameState.totalDartsUsed}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Final Score: $finalScore",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Stars Earned: $finalStars / 5",
                        style = MaterialTheme.typography.bodyLarge
                    )
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
                if (canUndo) {
                    Button(
                        onClick = onUndoLast,
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
                    "Are you sure you want to exit?",
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
fun AroundTheClockScreenPreview() {
    val navController = rememberNavController()
    val mockGameState = remember {
        mutableStateOf(
            AroundTheClockGameState(
                gameFinished = false,
                totalDartsUsed = 5,
                currentScore = 5,
                currentTarget = 6,
                throws = mutableListOf()
            )
        )
    }
    val mockGetGameData: (Int) -> Game? = remember {
        { gameId ->
            Game(
                id = gameId,
                name = "Mock Game",
                description = "This is a mock game for preview.",
                starThresholds = listOf(10, 20, 30, 40, 50),
                initialThrows = null
            )
        }
    }
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