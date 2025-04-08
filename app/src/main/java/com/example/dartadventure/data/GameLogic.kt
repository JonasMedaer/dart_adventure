package com.example.dartadventure.data

import com.example.dartadventure.chapters

// Update calculateScore to handle HighscoreDartThrow
fun calculateScore(throws: List<HighscoreDartThrow>): Int {
    return throws.sumOf { it.score }
}

fun calculateStars(score: Int, starThresholds: List<Int>): Int {
    return when {
        score >= starThresholds.getOrElse(4) { 300 } -> 5 // Use getOrElse for safety
        score >= starThresholds.getOrElse(3) { 250 } -> 4
        score >= starThresholds.getOrElse(2) { 200 } -> 3
        score >= starThresholds.getOrElse(1) { 150 } -> 2
        score >= starThresholds.getOrElse(0) { 100 } -> 1
        else -> 0
    }
}

fun calculateAroundTheClockScore(totalDarts: Int): Int {
    val baseScore = 1000
    val penaltyPerDart = 1
    val score = (baseScore - (totalDarts * penaltyPerDart)).coerceAtLeast(0) // Ensure score >= 0
    return score
}

fun updateHighscoreGameState(gameState: HighscoreGameState, throwScore: Int): HighscoreGameState {
    if (gameState.throwsRemaining <= 0) {
        return gameState
    }

    val newThrow = HighscoreDartThrow(throwScore) // Use HighscoreDartThrow
    val updatedThrows = gameState.throws.toMutableList().apply { add(newThrow) }
    val newScore = calculateScore(updatedThrows)
    val remaining = gameState.throwsRemaining - 1

    return gameState.copy(
        throwsRemaining = remaining,
        currentScore = newScore,
        throws = updatedThrows
    )
}

// Remove updateAroundTheClockGameState - logic moved to AroundTheClockScreen
/*
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
*/

fun <T : DartThrow> getLevelResult(gameState: GameState<T>): LevelResult {
    // Retrieve star thresholds based on chapter and game IDs
    val chapter = chapters.find { it.id == gameState.currentChapterId }
    val game = chapter?.games?.find { it.id == gameState.currentGameId }

    // Check if game is not null before accessing starThresholds
    val starThresholds =
        game?.starThresholds ?: emptyList() // Or handle the null case as appropriate

    val stars = if (starThresholds.isNotEmpty()) {
        calculateStars(gameState.currentScore, starThresholds)
    } else {
        0 // Or handle the case where thresholds are not found (e.g., log an error)
    }

    return LevelResult(
        gameState.currentChapterId,
        gameState.currentGameId,
        gameState.currentScore,
        stars
    )
}