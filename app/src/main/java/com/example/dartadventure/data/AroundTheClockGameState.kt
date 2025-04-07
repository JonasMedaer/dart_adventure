package com.example.dartadventure.data

data class AroundTheClockGameState(
    override val currentChapterId: Int = 1,
    override val currentGameId: Int = 1,
    override val currentScore: Int = 0,
    val currentTarget: Int = 1,
    val dartsThrown: Int = 0,
    override val throws: MutableList<DartThrow> = mutableListOf()
) : GameState(currentChapterId, currentGameId, currentScore, throws)