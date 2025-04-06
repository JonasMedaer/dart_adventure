package com.example.dartadventure

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dartadventure.ui.theme.DartAdventureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize StorageHelper (before any composables that might need it)
        StorageHelper.initialize(applicationContext)

        // Make the app fullscreen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Keep the screen on (if needed throughout the app, otherwise consider moving to Level1Screen)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Check if the device is a tablet and force portrait if not
        val isTablet =
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        if (!isTablet) {
            requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        setContent {
            DartAdventureTheme {
                val navController = rememberNavController() // Use rememberNavController directly
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home", // Or "select_level" if you want to skip the home screen
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            DartboardBackgroundWithContent(navController = navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController = navController)
                        }
                        composable("select_level") {
                            SelectLevelScreen(navController = navController)
                        }
                        composable("level1") {
                            Level1Screen(navController = navController)
                        }
                        // Add other levels here as needed
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        // Clear the flag when the activity is destroyed to allow the screen to turn off normally
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onDestroy()
    }
}



