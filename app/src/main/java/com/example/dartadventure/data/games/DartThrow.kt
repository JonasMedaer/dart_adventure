package com.example.dartadventure.data.games

sealed class DartThrow

data class HighscoreDartThrow(val score: Int) : DartThrow()

data class AroundTheClockDartThrow(val target: Int, val hit: Boolean) : DartThrow()