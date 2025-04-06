package com.example.dartadventure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Clear all level results from SharedPreferences
                clearAllScores()
                // Optionally, show a confirmation message to the user
            }
        ) {
            Text("Clear All Scores")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.popBackStack() // Navigate back
            }
        ) {
            Text("Back")
        }
    }
}

fun clearAllScores() {
    // Get all keys from SharedPreferences and remove the ones related to level results
    val prefs =
        StorageHelper.getSharedPreferences() // Assuming you have a function to get the SharedPreferences instance
    val editor = prefs.edit()
    for (key in prefs.all.keys) {
        if (key.startsWith("level_result_")) { // Assuming your keys start with "level_result_"
            editor.remove(key)
        }
    }
    editor.apply()
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val navController = rememberNavController()
    SettingsScreen(navController = navController)
}