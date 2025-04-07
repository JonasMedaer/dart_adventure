// ChapterData.kt
package com.example.dartadventure

import com.example.dartadventure.data.Chapter
import com.example.dartadventure.data.Game

val chapters = listOf(
    Chapter(
        id = 1,
        name = "The Beginning",
        description = "Learn the basics of dart throwing.",
        games = listOf(
            Game(
                id = 1,
                name = "Bullseye Challenge",
                description = "Hit as many bullseyes as you can!"
            ),
            Game(
                id = 2,
                name = "Around the Clock",
                description = "Hit the numbers 1 to 20 in order."
            )
        ),
        requiredStars = 5,
        isUnlocked = true
    ),
    Chapter(
        id = 2,
        name = "The Journey Continues",
        description = "Test your skills with more challenging games.",
        games = listOf(
            Game(
                id = 3,
                name = "Shanghai",
                description = "Score as many points as possible in 7 rounds."
            ),
            Game(
                id = 4,
                name = "Cricket",
                description = "Be the first to 'close' the numbers 15-20 and the bullseye."
            )
        ),
        requiredStars = 10
    )
)