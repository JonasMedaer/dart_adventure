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
fun ChapterSelectScreen(navController: NavController) {
    val chapters = chapters // Get chapters

    Column {
        Text("Select a Chapter")
        LazyColumn {
            items(chapters) { chapter ->
                Row {
                    Text(chapter.name)
                    Button(onClick = {
                        if (chapter.id == 1) {
                            navController.navigate("chapter1_games")
                        } else {
                            navController.navigate("game_select/${chapter.id}")
                        }
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
    ChapterSelectScreen(navController = navController)
}