package com.example.dartadventure.data

// Base GameState
open class GameState(
    open val currentChapterId: Int = 1,
    open val currentGameId: Int = 1,
    open val currentScore: Int = 0,
    open val throws: MutableList<DartThrow> = mutableListOf()
)