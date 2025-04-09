package com.example.dartadventure.utils

import android.content.SharedPreferences
import com.example.dartadventure.data.Chapter
import com.example.dartadventure.data.LevelResult

interface StorageInterface {
    fun saveLevelResult(newLevelResult: LevelResult)
    fun getLevelResult(chapterId: Int, gameId: Int): LevelResult?
    fun getSharedPreferences(): SharedPreferences? // Make this nullable as the mock might not need it
    fun calculateTotalStarsForChapter(chapter: Chapter): Int
    fun calculateMaxPossibleStarsForChapter(chapter: Chapter): Int
    fun calculateOverallStars(chapters: List<Chapter>): Pair<Int, Int>
}