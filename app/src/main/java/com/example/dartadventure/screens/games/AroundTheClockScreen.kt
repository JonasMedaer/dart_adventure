package com.example.dartadventure.screens.games

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
    var canUndo by remember { mutableStateOf(false) }
    var showGameOverDialog by remember { mutableStateOf(false) }
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
    var lastThrow by remember { mutableStateOf<AroundTheClockDartThrow?>(null) }

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

    LaunchedEffect(gameState.value.gameFinished) {
        if (gameState.value.gameFinished) {
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
            contentDescription = "Around The Clock Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TopSection(
                gameState = gameState.value,
                currentStars = currentStars,
                lastThrow = lastThrow
            )
            ActionButtons(
                gameState = gameState,
                dartsThisTurn = dartsThisTurn,
                onDartsThisTurnChange = { dartsThisTurn = it },
                starThresholds = starThresholds,
                onCanUndoChange = { canUndo = it }
            )
            BottomNavigation(
                onUndo = {
                    gameState.value = undoAroundTheClockGameState(gameState.value)
                    dartsThisTurn = gameState.value.dartsThrown % 3
                },
                canUndo = canUndo,
                onBack = { showExitDialog = true }
            )
        }
    }

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
            gameState.value = undoAroundTheClockGameState(gameState.value)
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
private fun TopSection(
    gameState: AroundTheClockGameState,
    currentStars: Int,
    lastThrow: AroundTheClockDartThrow?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Text(
            "Around the Clock",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                    CircleShape
                )
                .padding(48.dp)
                .width(160.dp)
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${gameState.currentTarget}",
                style = TextStyle(
                    fontSize = 120.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Clip
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Darts Used: ${gameState.totalDartsUsed}",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Text(
            "Stars: $currentStars",
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
        Box(modifier = Modifier.height(lineHeightDp)) {
            if (lastThrow != null) {
                Text(
                    "Last Action: ${if (lastThrow.hit) "Hit" else "Miss"} on ${lastThrow.target}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Yellow
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
    onCanUndoChange: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
    ) {
        Button(
            onClick = {
                if (dartsThisTurn < 3 && !gameState.value.gameFinished) {
                    gameState.value = updateAroundTheClockGameState(
                        gameState.value,
                        true
                    )
                    onDartsThisTurnChange(dartsThisTurn + 1)
                    onCanUndoChange(true) // Enable undo after an action
                }
                if (dartsThisTurn == 2) {
                    onDartsThisTurnChange(0)
                }
            },
            enabled = !gameState.value.gameFinished,
            modifier = Modifier
                .weight(1f)
                .height(72.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Hit", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        Button(
            onClick = {
                if (dartsThisTurn < 3 && !gameState.value.gameFinished) {
                    gameState.value = updateAroundTheClockGameState(
                        gameState.value,
                        false
                    )
                    onDartsThisTurnChange(dartsThisTurn + 1)
                    onCanUndoChange(true) // Enable undo after an action
                }
                if (dartsThisTurn == 2) {
                    onDartsThisTurnChange(0)
                }
            },
            enabled = !gameState.value.gameFinished,
            modifier = Modifier
                .weight(1f)
                .height(72.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
        ) {
            Text("Miss", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun BottomNavigation(onUndo: () -> Unit, canUndo: Boolean, onBack: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(bottom = 24.dp) // Add bottom padding here
    ) {
        Button(
            onClick = onUndo,
            enabled = canUndo,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
        ) {
            Text("Undo", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(60.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Back", style = MaterialTheme.typography.titleMedium)
        }
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
                        "Stars Earned: $finalStars",
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
                    "Are you sure you want to exit? Your progress will be saved.",
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