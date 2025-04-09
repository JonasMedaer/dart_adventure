package com.example.dartadventure.data.games.aroundtheclock

import com.example.dartadventure.data.games.AroundTheClockDartThrow
import com.example.dartadventure.data.games.GameState

data class AroundTheClockGameState(
    override val currentChapterId: Int = 1,
    override val currentGameId: Int = 1,
    override val currentScore: Int = 10000,
    val currentTarget: Int = 1,
    val dartsThrown: Int = 0,
    val totalDartsUsed: Int = 0,
    val mustEndOnBullseye: Boolean = false,
    val gameFinished: Boolean = false,
    val initialized: Boolean = false,
    override val throws: MutableList<AroundTheClockDartThrow> = mutableListOf()
) : GameState<AroundTheClockDartThrow>( // Specify AroundTheClockDartThrow
    currentChapterId,
    currentGameId,
    currentScore,
    throws
)
// Around the clock starts with 10000 points
// scoring with all darts will result in a perfect score of 9800