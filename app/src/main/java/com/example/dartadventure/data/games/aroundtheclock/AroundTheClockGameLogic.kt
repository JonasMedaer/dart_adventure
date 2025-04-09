package com.example.dartadventure.data.aroundtheclock

fun calculateAroundTheClockScore(totalDarts: Int): Int {
    val baseScore = 10000
    val penaltyPerDart = 10
    val score = (baseScore - (totalDarts * penaltyPerDart)).coerceAtLeast(0) // Ensure score >= 0
    return score
}