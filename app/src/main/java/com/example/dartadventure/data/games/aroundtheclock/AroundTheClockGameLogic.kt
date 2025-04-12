package com.example.dartadventure.data.aroundtheclock

import com.example.dartadventure.data.games.AroundTheClockDartThrow
import com.example.dartadventure.data.games.aroundtheclock.AroundTheClockGameState

fun calculateAroundTheClockScore(totalDarts: Int): Int {
    val baseScore = 10000
    val penaltyPerDart = 10
    val score = (baseScore - (totalDarts * penaltyPerDart)).coerceAtLeast(0) // Ensure score >= 0
    return score
}

fun updateAroundTheClockGameState(
    gameState: AroundTheClockGameState,
    hit: Boolean
): AroundTheClockGameState {
    val newTotalDartsUsed = gameState.totalDartsUsed + 1
    val newCurrentScore = calculateAroundTheClockScore(newTotalDartsUsed)
    val newThrow = AroundTheClockDartThrow(gameState.currentTarget, hit)
    val newThrows = gameState.throws.toMutableList().apply {
        add(newThrow)
    }
    val newCurrentTarget =
        if (hit && gameState.currentTarget < if (gameState.mustEndOnBullseye) 25 else 20) {
            if (gameState.currentTarget == 20) 25 else gameState.currentTarget + 1
        } else {
            gameState.currentTarget
        }
    val gameFinished = if (gameState.mustEndOnBullseye) {
        hit && gameState.currentTarget == 25
    } else {
        hit && gameState.currentTarget >= 20
    }
    return gameState.copy(
        totalDartsUsed = newTotalDartsUsed,
        currentScore = newCurrentScore,
        currentTarget = newCurrentTarget,
        throws = newThrows,
        gameFinished = gameFinished
    )
}

fun undoAroundTheClockGameState(gameState: AroundTheClockGameState): AroundTheClockGameState {
    if (gameState.throws.isEmpty()) {
        return gameState // Nothing to undo
    }

    val lastThrow = gameState.throws.last()
    val previousThrows = gameState.throws.dropLast(1).toMutableList() // Convert to MutableList
    val newTotalDartsUsed = gameState.totalDartsUsed - 1
    val newCurrentScore = calculateAroundTheClockScore(newTotalDartsUsed)
    val newCurrentTarget =
        if (lastThrow.hit) gameState.currentTarget - 1 else gameState.currentTarget
    val newGameFinished = false // After undo, the game is no longer finished (if it was)
    val newDartsThrown = gameState.dartsThrown - 1

    return gameState.copy(
        currentTarget = newCurrentTarget.coerceAtLeast(1), // Ensure target doesn't go below 1
        totalDartsUsed = newTotalDartsUsed.coerceAtLeast(0),
        currentScore = newCurrentScore,
        throws = previousThrows,
        gameFinished = newGameFinished,
        dartsThrown = newDartsThrown.coerceAtLeast(0)
    )
}