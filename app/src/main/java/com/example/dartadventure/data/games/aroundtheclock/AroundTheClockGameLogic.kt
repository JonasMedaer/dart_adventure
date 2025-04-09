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
    val newThrows = gameState.throws.toMutableList().apply {
        add(AroundTheClockDartThrow(gameState.currentTarget, hit))
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