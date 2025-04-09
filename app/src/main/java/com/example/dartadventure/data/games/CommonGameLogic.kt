package com.example.dartadventure.data.games

import com.example.dartadventure.chapters
import com.example.dartadventure.data.LevelResult

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