package com.example.dartadventure.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.R
import com.example.dartadventure.createMockGame
import com.example.dartadventure.data.Chapter
import com.example.dartadventure.utils.StorageHelper
import com.example.dartadventure.utils.StorageHelper.calculateMaxPossibleStarsForChapter

@Composable
fun ChapterSelectScreen(
    navController: NavController,
    chapters: List<Chapter> = com.example.dartadventure.chapters,
    calculateTotalStars: (Chapter) -> Int = { chapter ->
        StorageHelper.calculateTotalStarsForChapter(chapter)
    },
    calculateOverallStars: (List<Chapter>) -> Pair<Int, Int> = { chapters ->
        val totalCurrentStars = chapters.sumOf { calculateTotalStars(it) }
        val totalPossibleStars = chapters.sumOf { calculateMaxPossibleStarsForChapter(it) }
        Pair(totalCurrentStars, totalPossibleStars)
    }
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet =
        configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    val backgroundImage = if (isLandscape || isTablet) {
        R.drawable.chapterselect_0_tablet
    } else {
        R.drawable.chapterselect_0
    }
    val (overallCurrentStars, overallPossibleStars) = calculateOverallStars(chapters)
    // Define unlock thresholds for each chapter (you can adjust these)
    val unlockThresholds = mapOf(
        1 to 0,   // Chapter 1 unlocks at 0 stars (always unlocked)
        2 to 6,   // Chapter 2 unlocks at 5 stars
        3 to 15,  // Chapter 3 unlocks at 15 stars
        // ... add more chapters and thresholds as needed
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Chapter Select Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Select a Chapter")
            Text("Total Stars: $overallCurrentStars / $overallPossibleStars")
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(chapters) { chapter ->
                    val isUnlocked =
                        unlockThresholds[chapter.id]?.let { overallCurrentStars >= it } ?: true
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(chapter.name)
                            val totalStars = calculateTotalStars(chapter)
                            val maxPossibleStars = calculateMaxPossibleStarsForChapter(chapter)
                            val displayText = if (isUnlocked) {
                                "Total Stars: $totalStars / $maxPossibleStars"
                            } else {
                                val requiredStars = unlockThresholds[chapter.id] ?: 0
                                "Total Stars: $totalStars / $maxPossibleStars (Requires $requiredStars overall)"
                            }
                            Text(displayText)
                        }
                        Button(
                            onClick = {
                                navController.navigate("game_select/${chapter.id}")
                            },
                            enabled = isUnlocked
                        ) {
                            Text(if (isUnlocked) "Select" else "Locked")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun ChapterSelectScreenPreview() {
    val navController = rememberNavController()
    val mockChapters = remember {
        listOf(
            Chapter(1, "Chapter 1", "Desc 1", listOf(createMockGame(1, "Game 1", "Desc")), 0),
            Chapter(2, "Chapter 2", "Desc 2", listOf(createMockGame(2, "Game 2", "Desc")), 6)
        )
    }
    val mockCalculateStars: (Chapter) -> Int = { chapter ->
        when (chapter.id) {
            1 -> 3
            2 -> 0
            else -> 0
        }
    }
    val mockCalculateOverallStars: (List<Chapter>) -> Pair<Int, Int> = { chapters ->
        val totalCurrent = chapters.sumOf { mockCalculateStars(it) }
        val totalPossible = chapters.sumOf { calculateMaxPossibleStarsForChapter(it) }
        Pair(totalCurrent, totalPossible)
    }
    ChapterSelectScreen(
        navController = navController,
        chapters = mockChapters,
        calculateTotalStars = mockCalculateStars,
        calculateOverallStars = mockCalculateOverallStars
    )
}