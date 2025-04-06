package com.example.dartadventure

import android.content.Context
import android.content.SharedPreferences
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
        val key = "$LEVEL_RESULT_KEY_PREFIX${levelResult.level}"
        val json = gson.toJson(levelResult)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getLevelResult(level: Int): LevelResult? {
        val key = "$LEVEL_RESULT_KEY_PREFIX$level"
        val json = sharedPreferences.getString(key, null) ?: return null
        return gson.fromJson(json, LevelResult::class.java)
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}