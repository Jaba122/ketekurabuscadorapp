package com.example.ketekura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ketekura.ui.theme.KetekuraTheme
import com.example.ketekura.view.AttentionSearchScreen
import com.example.ketekura.view.LoginScreen
import com.example.ketekura.view.MainContentScreen
import com.example.ketekura.view.PatientHistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KetekuraTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = { username ->
                navController.navigate("main/$username") { // Pass username as an argument
                    popUpTo("login") { inclusive = true } // Clear login screen from back stack
                }
            })
        }
        composable(
            "main/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            MainContentScreen(
                username = username,
                onNavigateToAttentionSearch = { navController.navigate("attention_search") },
                onNavigateToPatientHistory = { navController.navigate("patient_history") }
            )
        }
        composable("attention_search") {
            AttentionSearchScreen()
        }
        composable("patient_history") {
            PatientHistoryScreen()
        }
    }
}
