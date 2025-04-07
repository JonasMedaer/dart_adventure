package com.example.dartadventure.data

data class Chapter(
    val id: Int,
    val name: String,
    val description: String,
    val games: List<Game>,
    val requiredStars: Int,
    val isUnlocked: Boolean = false
)