package com.example.dartadventure.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.chapters

@Composable
fun GameSelectScreen(navController: NavController, chapterId: Int) {
    val chapter =
        chapters.find { it.id == chapterId } ?: return // Handle invalid chapterId
    Column {
        Text("Chapter: ${chapter.name}")
        LazyColumn {
            items(chapter.games) { game ->
                Row {
                    Text(game.name)
                    Text("Highscore: ${game.highscore}, Stars: ${game.stars}")
                    Button(onClick = {
                        when (game.id) {
                            // Update navigation to match your game screens
                            1 -> navController.navigate("bullseye_challenge") // Example
                            2 -> navController.navigate("around_the_clock") // Example
                            // Add navigation for other games in this chapter
                        }
                    }) {
                        Text("Play")
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
fun GameSelectScreenPreview() {
    val navController = rememberNavController()
    // Assuming you want to preview Chapter 1
    GameSelectScreen(navController = navController, chapterId = 1)
}