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
                name = "Highscore",
                description = "Score as many points as you can!",
                starThresholds = listOf(100, 150, 200, 250, 300),
                initialThrows = 5
            ),
            Game(
                id = 2,
                name = "Around the Clock",
                description = "Hit the numbers 1 to 20 in order.",
                starThresholds = listOf( // Base score - (20 numbers * (amount of darts * 10))
                    10000 - (20 * 40), //9200
                    10000 - (20 * 35), //9300
                    10000 - (20 * 30), //9400
                    10000 - (20 * 25), //9500
                    10000 - (20 * 20) //9600
                ),
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
                description = "Score as many points as possible in 7 rounds.",
                starThresholds = listOf(120, 180, 240, 300, 360),
            ),
            Game(
                id = 4,
                name = "Cricket",
                description = "Be the first to 'close' the numbers 15-20 and the bullseye.",
                starThresholds = listOf(
                    5,
                    7,
                    9,
                    11,
                    13
                ),
                initialThrows = 5
            )
        ),
        requiredStars = 10
    )
)

fun createMockGame(id: Int, name: String, description: String): Game {
    return Game(
        id = id,
        name = name,
        description = description,
        starThresholds = listOf(100, 150, 200, 250, 300),
        initialThrows = 5
    )
}