package com.example.ketekura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ketekura.ui.theme.KetekuraTheme
import com.example.ketekura.view.AdminDashboard
import com.example.ketekura.view.LoginScreen
import com.example.ketekura.view.PatientDashboardScreen

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
    // La app ahora SIEMPRE empieza en la pantalla de Login
    NavHost(navController = navController, startDestination = "login") {

        // 1. Ruta de Login Unificada
        composable("login") {
            LoginScreen(onLoginSuccess = { role ->
                // Dependiendo del rol, navegamos a una pantalla u otra
                val destination = when (role) {
                    "ADMIN" -> "admin_dashboard"
                    "PACIENTE" -> "patient_dashboard"
                    else -> "login" // Si el rol es desconocido, volvemos al login
                }
                // Navegamos a la pantalla de destino y limpiamos la pila de navegación
                navController.navigate(destination) {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        // 2. Ruta para el Panel de Administrador
        composable("admin_dashboard") {
            AdminDashboard()
        }

        // 3. Ruta para el Panel de Paciente
        composable("patient_dashboard") {
            PatientDashboardScreen()
        }
    }
}
