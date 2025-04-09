package com.example.dartadventure.utils

import android.content.SharedPreferences
import com.example.dartadventure.data.Chapter
import com.example.dartadventure.data.LevelResult
import com.google.gson.Gson

object MockStorageHelper : StorageInterface {
    private const val LEVEL_RESULT_KEY_PREFIX = "level_result_"
    private val mockLevelResults = mutableMapOf<String, LevelResult>()
    private val gson = Gson()
    private var isInitialized = false

    fun initialize() {
        isInitialized = true
        println("MockStorageHelper initialized")
    }

    override fun saveLevelResult(newLevelResult: LevelResult) {
        if (!isInitialized) {
            println("Warning: MockStorageHelper not initialized!")
            return
        }
        val key = "$LEVEL_RESULT_KEY_PREFIX${newLevelResult.chapter}_${newLevelResult.game}"
        val existingResult = mockLevelResults[key]
        if (existingResult == null || newLevelResult.score > existingResult.score) {
            mockLevelResults[key] = newLevelResult
            println("MockStorageHelper saved: $newLevelResult with key: $key")
        } else {
            println("MockStorageHelper: New score not higher, not updating for key: $key")
        }
    }

    override fun getLevelResult(chapterId: Int, gameId: Int): LevelResult? {
        val key = "$LEVEL_RESULT_KEY_PREFIX${chapterId}_${gameId}"
        val result = mockLevelResults[key]
        println("MockStorageHelper retrieved for key: $key -> $result")
        return result
    }

    override fun getSharedPreferences(): SharedPreferences? {
        return null // Or throw an UnsupportedOperationException if your mock doesn't need this
    }

    override fun calculateTotalStarsForChapter(chapter: Chapter): Int {
        return chapter.games.sumOf { game ->
            getLevelResult(chapter.id, game.id)?.stars ?: 0
        }
    }

    override fun calculateMaxPossibleStarsForChapter(chapter: Chapter): Int {
        return chapter.games.sumOf { it.starThresholds.size }
    }

    override fun calculateOverallStars(chapters: List<Chapter>): Pair<Int, Int> {
        val totalCurrentStars = chapters.sumOf { calculateTotalStarsForChapter(it) }
        val totalPossibleStars = chapters.sumOf { calculateMaxPossibleStarsForChapter(it) }
        return Pair(totalCurrentStars, totalPossibleStars)
    }
}