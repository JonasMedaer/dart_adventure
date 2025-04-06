package com.example.dartadventure

class GameData {
}

data class DartThrow(val score: Int)

data class LevelResult(val level: Int, val score: Int, val stars: Int)

data class GameState(
    val currentLevel: Int = 1,
    val throwsRemaining: Int = 5,
    val currentScore: Int = 0,
    val throws: MutableList<DartThrow> = mutableListOf()
)

fun calculateScore(throws: List<DartThrow>): Int {
    return throws.sumOf { it.score }
}

fun calculateStars(score: Int): Int {
    // Define your star rating logic here.  This is just an example.
    return when {
        score >= 300 -> 5
        score >= 250 -> 4
        score >= 200 -> 3
        score >= 150 -> 2
        score >= 100 -> 1
        else -> 0
    }
}

fun updateGameState(gameState: GameState, throwScore: Int): GameState {
    if (gameState.throwsRemaining <= 0) {
        return gameState // No throws remaining, game over
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

fun getLevelResult(gameState: GameState): LevelResult {
    val stars = calculateStars(gameState.currentScore)
    return LevelResult(gameState.currentLevel, gameState.currentScore, stars)
}