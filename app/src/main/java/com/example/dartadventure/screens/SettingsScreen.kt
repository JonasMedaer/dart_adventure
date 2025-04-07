package com.example.dartadventure.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.utils.StorageHelper

@Composable
fun SettingsScreen(navController: NavController) {
    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                showDialog.value = true
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

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Clear Scores") },
            text = { Text("Are you sure you want to clear all saved scores?") },
            confirmButton = {
                Button(
                    onClick = {
                        clearAllScores()
                        showDialog.value = false
                        // Optionally, show a confirmation message (e.g., a Snackbar)
                    }
                ) {
                    Text("Clear")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun clearAllScores() {
    val prefs = StorageHelper.getSharedPreferences()
    val editor = prefs.edit()
    for (key in prefs.all.keys) {
        if (key.startsWith("level_result_")) {
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