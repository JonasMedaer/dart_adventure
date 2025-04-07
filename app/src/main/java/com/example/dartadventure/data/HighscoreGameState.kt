package com.example.dartadventure.data

data class HighscoreGameState(
    override val currentChapterId: Int = 1,
    override val currentGameId: Int = 1,
    override val currentScore: Int = 0,
    val throwsRemaining: Int, // This is a required parameter
    override val throws: MutableList<DartThrow> = mutableListOf()
) : GameState(currentChapterId, currentGameId, currentScore, throws)