package com.example.dartadventure.data.games.highscore

import com.example.dartadventure.data.games.GameState
import com.example.dartadventure.data.games.HighscoreDartThrow

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