package com.example.dartadventure.data

import com.example.dartadventure.data.games.Game

data class Chapter(
    val id: Int,
    val name: String,
    val description: String,
    val games: List<Game>,
    val requiredStars: Int,
    val isUnlocked: Boolean = false
)