package com.example.dartadventure.data

data class HighscoreGameState(
    override val currentChapterId: Int = 1,
    override val currentGameId: Int = 1,
    override val currentScore: Int = 0,
    val throwsRemaining: Int,
    override val throws: MutableList<HighscoreDartThrow> = mutableListOf()
) : GameState<HighscoreDartThrow>( // Specify HighscoreDartThrow
    currentChapterId,
    currentGameId,
    currentScore,
    throws
)