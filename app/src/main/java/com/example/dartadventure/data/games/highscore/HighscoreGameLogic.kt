package com.example.dartadventure.data.highscore

import com.example.dartadventure.data.games.HighscoreDartThrow
import com.example.dartadventure.data.games.highscore.HighscoreGameState

fun calculateScore(throws: List<HighscoreDartThrow>): Int {
    return throws.sumOf { it.score }
}

fun updateHighscoreGameState(gameState: HighscoreGameState, throwScore: Int): HighscoreGameState {
    if (gameState.throwsRemaining <= 0) {
        return gameState
    }
    val newThrow = HighscoreDartThrow(throwScore)
    val updatedThrows = gameState.throws.toMutableList().apply { add(newThrow) }
    val newScore = calculateScore(updatedThrows)
    val remaining = gameState.throwsRemaining - 1
    return gameState.copy(
        throwsRemaining = remaining,
        currentScore = newScore,
        throws = updatedThrows
    )
}

fun undoHighscoreGameState(gameState: HighscoreGameState): HighscoreGameState {
    if (gameState.throws.isEmpty()) {
        return gameState // Nothing to undo
    }

    val lastThrow = gameState.throws.last()
    val previousThrows = gameState.throws.dropLast(1).toMutableList()
    val newScore = calculateScore(previousThrows)
    val newThrowsRemaining = gameState.throwsRemaining + 1

    return gameState.copy(
        currentScore = newScore,
        throwsRemaining = newThrowsRemaining,
        throws = previousThrows
    )
}