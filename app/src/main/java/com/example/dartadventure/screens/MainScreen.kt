package com.example.dartadventure.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.R
import com.example.dartadventure.ui.theme.DartAdventureTheme

@Composable
fun DartboardBackgroundWithContent(navController: NavController, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val backgroundImage =
        if (isLandscape) R.drawable.startup_path_tablet else R.drawable.startup_path
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = "Dartboard Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Use Box to position elements independently
        Box(
            modifier = Modifier.fillMaxSize(), // Make the Box fill the entire screen
        ) {
            // Top-centered Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 320.dp)
            ) {
                Text(
                    text = "Dart Adventure",
                    style = TextStyle(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                    )
                )

            }
            // Bottom-centered Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -70.dp)
            ) {

                Spacer(modifier = Modifier.height(100.dp))
                Button(
                    onClick = { navController.navigate("chapter_select") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF4CAF50),
                                    Color(0xFF1B5E20),
                                    Color(0xFF4CAF50)
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(2.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                ) {
                    Text(
                        "Start Game",
                        fontSize = 50.sp,
                    )

                }
                Button(onClick = {
                    navController.navigate("settings")
                }) {
                    Text("Settings")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(name = "Pixel 7 pro", device = Devices.PIXEL_7_PRO)
@Preview(name = "Tablet", device = Devices.PIXEL_C)
@Composable
fun DartboardPreview() {
    DartAdventureTheme {
        val navController = rememberNavController() // Create a NavController for the preview
        DartboardBackgroundWithContent(navController = navController)
    }
}