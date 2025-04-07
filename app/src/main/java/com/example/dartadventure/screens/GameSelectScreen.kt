package com.example.dartadventure.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.chapters
import com.example.dartadventure.data.LevelResult
import com.example.dartadventure.utils.StorageHelper

@Composable
fun GameSelectScreen(
    navController: NavController,
    chapterId: Int,
    getLevelResult: (Int) -> LevelResult? = { gameId ->
        StorageHelper.getLevelResult(chapterId, gameId)
    }
) {
    val chapter = chapters.find { it.id == chapterId } ?: return
    Column {
        Text("Chapter: ${chapter.name}")
        // Delegate content display based on chapterId, passing necessary data
        when (chapterId) {
            1 -> Chapter1GamesList(navController, chapter, getLevelResult)
            // Add more cases for other chapters if they have unique layouts
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun GameSelectScreenPreview() {
    val navController = rememberNavController()
    // Mock LevelResult data
    val mockLevelResults = mapOf(
        1 to LevelResult(chapter = 1, game = 1, score = 100, stars = 2),
        2 to LevelResult(chapter = 1, game = 2, score = 150, stars = 3)
    )
    GameSelectScreen(
        navController = navController,
        chapterId = 1,
        getLevelResult = { gameId -> mockLevelResults[gameId] } // Provide mock data
    )
}