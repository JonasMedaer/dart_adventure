// data/GameLogic.kt  (or a more descriptive name)
package com.example.dartadventure.data

fun calculateScore(throws: List<DartThrow>): Int {
    return throws.sumOf { it.score }
}

fun calculateStars(score: Int): Int {
    return when {
        score >= 300 -> 5
        score >= 250 -> 4
        score >= 200 -> 3
        score >= 150 -> 2
        score >= 100 -> 1
        else -> 0
    }
}

fun updateHighscoreGameState(gameState: HighscoreGameState, throwScore: Int): HighscoreGameState {
    if (gameState.throwsRemaining <= 0) {
        return gameState
    }

    val newThrow = DartThrow(throwScore)
    val updatedThrows = gameState.throws.toMutableList().apply { add(newThrow) }
    val newScore = calculateScore(updatedThrows)
    val remaining = gameState.throwsRemaining - 1

    return gameState.copy(
        throwsRemaining = remaining,
        currentScore = newScore,
        throws = updatedThrows
    )
}

fun updateAroundTheClockGameState(
    gameState: AroundTheClockGameState,
    throwResult: ThrowResult
): AroundTheClockGameState {
    var newScore = gameState.currentScore
    var newTarget = gameState.currentTarget
    if (throwResult.hitNumber == newTarget) {
        newScore += 10
        newTarget++
    }
    return gameState.copy(
        currentScore = newScore,
        currentTarget = newTarget,
        dartsThrown = gameState.dartsThrown + 1,
        throws = gameState.throws.toMutableList()
            .apply { add(DartThrow(throwResult.score)) }
    )
}

fun getLevelResult(gameState: GameState): LevelResult {
    val stars = calculateStars(gameState.currentScore)
    return LevelResult(
        gameState.currentChapterId,
        gameState.currentGameId,
        gameState.currentScore,
        stars
    )
}