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
import com.example.ketekura.view.PatientDashboardScreen
import com.example.ketekura.view.PatientHistoryScreen
import com.example.ketekura.view.PatientLoginScreen
import com.example.ketekura.view.RoleSelectionScreen

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
    NavHost(navController = navController, startDestination = "role_selection") { 
        composable("role_selection") {
            RoleSelectionScreen(
                onNavigateToAdminLogin = { navController.navigate("admin_login") },
                onNavigateToPatientLogin = { navController.navigate("patient_login") }
            )
        }

        composable("admin_login") { 
            LoginScreen(onLoginSuccess = { username ->
                navController.navigate("main/$username") {
                    popUpTo("admin_login") { inclusive = true }
                }
            })
        }

        composable("patient_login") {
            PatientLoginScreen(onLoginSuccess = { userRut ->
                navController.navigate("patient_dashboard/$userRut") {
                    popUpTo("patient_login") { inclusive = true }
                }
            })
        }

        composable(
            "patient_dashboard/{rut}", 
            arguments = listOf(navArgument("rut") { type = NavType.StringType })
        ) { backStackEntry ->
            val rut = backStackEntry.arguments?.getString("rut") ?: ""
            PatientDashboardScreen(rut = rut)
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
