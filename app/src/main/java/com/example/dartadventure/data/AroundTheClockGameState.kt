package com.example.dartadventure.data

data class AroundTheClockGameState(
    override val currentChapterId: Int = 1,
    override val currentGameId: Int = 1,
    override val currentScore: Int = 1020,
    val currentTarget: Int = 1,
    val dartsThrown: Int = 0,
    val totalDartsUsed: Int = 0,
    override val throws: MutableList<AroundTheClockDartThrow> = mutableListOf()
) : GameState<AroundTheClockDartThrow>( // Specify AroundTheClockDartThrow
    currentChapterId,
    currentGameId,
    currentScore,
    throws
)
// Around the clock starts with 1020 points
// scoring with all darts will result in a perfect score of 1000