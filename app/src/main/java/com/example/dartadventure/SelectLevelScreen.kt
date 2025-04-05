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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SelectLevelScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select a Level")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            // Navigate to Level 1 (replace with actual navigation logic)
            // Example: navController.navigate("level1")
        }) {
            Text("Level 1")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Navigate to Level 2
            // Example: navController.navigate("level2")
        }) {
            Text("Level 2")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Navigate to Level 3
            // Example: navController.navigate("level3")
        }) {
            Text("Level 3")
        }
    }
}


