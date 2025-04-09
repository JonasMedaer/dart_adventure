package com.example.dartadventure.data.games

data class Game(
    val id: Int,
    val name: String,
    val description: String,
    val highscore: Int = 0,
    val stars: Int = 0,
    val starThresholds: List<Int>,
    val initialThrows: Int? = null
)