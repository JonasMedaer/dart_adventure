package com.example.dartadventure.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.dartadventure.data.Chapter
import com.example.dartadventure.data.LevelResult
import com.google.gson.Gson

object StorageHelper {
    private const val PREFS_NAME = "DartAdventurePrefs"
    private const val LEVEL_RESULT_KEY_PREFIX = "level_result_"

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveLevelResult(levelResult: LevelResult) {
        val key =
            "$LEVEL_RESULT_KEY_PREFIX${levelResult.chapter}_${levelResult.game}" // Composite key
        val json = gson.toJson(levelResult)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getLevelResult(chapterId: Int, gameId: Int): LevelResult? { // Modified parameters
        val key = "$LEVEL_RESULT_KEY_PREFIX${chapterId}_${gameId}" // Composite key
        val json = sharedPreferences.getString(key, null) ?: return null
        return gson.fromJson(json, LevelResult::class.java)
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }

    fun calculateTotalStarsForChapter(chapter: Chapter): Int {
        return chapter.games.sumOf { game ->
            getLevelResult(chapter.id, game.id)?.stars ?: 0
        }
    }
}