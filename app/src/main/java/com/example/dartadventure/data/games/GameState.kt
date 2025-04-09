package com.example.dartadventure.data.games

abstract class GameState<T : DartThrow>( // Make GameState generic
    open val currentChapterId: Int,
    open val currentGameId: Int,
    open val currentScore: Int,
    open val throws: MutableList<T> // Use the generic type T
)