package com.example.notetaker.android.ui

import androidx.compose.runtime.Composable
import com.example.notetaker.android.ui.NoteTakingScreen
import com.example.notetaker.android.ui.NoteDashboard
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NoteApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { NoteDashboard(navController) }
        composable("create_note") { NoteTakingScreen(navController) }
    }
}