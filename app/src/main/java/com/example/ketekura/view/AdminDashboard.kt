package com.example.ketekura.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ketekura.viewmodel.AdminViewModel

@Composable
fun AdminDashboard(
    navController: NavController, 
    adminViewModel: AdminViewModel = viewModel()
) {
    val adminNavController = rememberNavController()

    Scaffold(
        topBar = {
            KetekuraTopAppBar(
                title = "Panel de Administrador",
                canNavigateBack = false,
                navController = navController
            )
        },
        bottomBar = {
            AdminBottomNavigationBar(navController = adminNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = adminNavController,
            startDestination = AdminBottomNavItem.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AdminBottomNavItem.Search.route) { AdminSearchScreen(adminViewModel) }
            composable(AdminBottomNavItem.QRScanner.route) { AdminQRScannerScreen() }
        }
    }
}
