package com.example.dartadventure

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun SelectLevelScreen(
    navController: NavController,
    getLevelResult: (Int) -> LevelResult? = { level -> StorageHelper.getLevelResult(level) }
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val backgroundImage =
        if (isLandscape) R.drawable.levelselect_path_tablet else R.drawable.levelselect_path // Replace with your actual image names

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Level Select Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(), // Fill the Box
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Select a Level")
            Spacer(modifier = Modifier.height(24.dp))

            // Level 1 Button and Highscore/Stars
            val level1Result = getLevelResult(1)
            val level1Highscore = level1Result?.score ?: 0
            val level1Stars = level1Result?.stars ?: 0
            Button(onClick = {
                navController.navigate("level1")
            }) {
                Text("Level 1")
            }
            Text("Highscore: $level1Highscore, Stars: $level1Stars")

            Spacer(modifier = Modifier.height(16.dp))

            // Level 2 Button and Highscore/Stars
            val level2Result = getLevelResult(2)
            val level2Highscore = level2Result?.score ?: 0
            val level2Stars = level2Result?.stars ?: 0
            Button(onClick = {
                // Navigate to Level 2
                // Example: navController.navigate("level2")
            }) {
                Text("Level 2")
            }
            Text("Highscore: $level2Highscore, Stars: $level2Stars")

            Spacer(modifier = Modifier.height(16.dp))

            // Level 3 Button and Highscore/Stars
            val level3Result = getLevelResult(3)
            val level3Highscore = level3Result?.score ?: 0
            val level3Stars = level3Result?.stars ?: 0
            Button(onClick = {
                // Navigate to Level 3
                // Example: navController.navigate("level3")
            }) {
                Text("Level 3")
            }
            Text("Highscore: $level3Highscore, Stars: $level3Stars")
        }
    }
}

private fun getMockLevelResult(level: Int): LevelResult? {
    // Return a mock LevelResult for preview purposes
    return when (level) {
        1 -> LevelResult(level = 1, score = 120, stars = 4)
        2 -> LevelResult(level = 2, score = 80, stars = 2)
        3 -> LevelResult(level = 3, score = 150, stars = 5)
        else -> null
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 Pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun SelectLevelScreenPreview() {
    val navController = rememberNavController()

    // Use a modified SelectLevelScreen composable for the preview
    SelectLevelScreen(
        navController = navController,
        getLevelResult = { level -> getMockLevelResult(level) }
    )
}
