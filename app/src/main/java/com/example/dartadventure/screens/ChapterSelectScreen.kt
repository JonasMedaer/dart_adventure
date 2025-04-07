package com.example.dartadventure.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.data.Chapter
import com.example.dartadventure.data.Game
import com.example.dartadventure.utils.StorageHelper

@Composable
fun ChapterSelectScreen(
    navController: NavController,
    chapters: List<Chapter> = com.example.dartadventure.chapters, // Default to your actual chapters
    calculateTotalStars: (Chapter) -> Int = { chapter ->
        StorageHelper.calculateTotalStarsForChapter(
            chapter
        )
    } // Default to your actual function
) {
    Column {
        Text("Select a Chapter")
        LazyColumn {
            items(chapters) { chapter ->
                Row {
                    Column {
                        Text(chapter.name)
                        val totalStars = calculateTotalStars(chapter)
                        Text("Total Stars: $totalStars / ${chapter.requiredStars}")
                    }
                    Button(onClick = {
                        navController.navigate("game_select/${chapter.id}")
                    }) {
                        Text("Select")
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
    // Mock chapters with some star data for the preview
    val mockChapters = remember {
        listOf(
            Chapter(1, "Chapter 1", "Desc 1", listOf(Game(1, "Game 1", "Desc")), 5, true),
            Chapter(2, "Chapter 2", "Desc 2", listOf(Game(2, "Game 2", "Desc")), 10, false)
        )
    }
    // Mock the calculateTotalStarsForChapter function for the preview
    val mockCalculateStars: (Chapter) -> Int = { chapter ->
        when (chapter.id) {
            1 -> 3
            2 -> 0
            else -> 0
        }
    }

    // Call the ChapterSelectScreen composable, providing mock data
    ChapterSelectScreen(
        navController = navController,
        chapters = mockChapters, // Pass the mock chapters
        calculateTotalStars = mockCalculateStars // Pass the mock calculation function
    )
}