package com.example.ketekura.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ketekura.viewmodel.PatientViewModel

@Composable
fun PatientDashboardScreen(
    navController: NavController, // El controlador principal para cerrar sesión
    patientViewModel: PatientViewModel = viewModel()
) {
    val patientNavController = rememberNavController() // Controlador para la navegación interna
    val snackbarHostState = remember { SnackbarHostState() }

    // Recolectamos el mensaje del Snackbar desde el ViewModel
    val snackbarMessage by patientViewModel.snackbarMessage.collectAsState()

    // Este efecto se lanzará CADA VEZ que el mensaje del snackbar cambie
    LaunchedEffect(snackbarMessage) {
        // Si hay un mensaje para mostrar, lo lanzamos
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message = message)
            // Una vez mostrado, notificamos al ViewModel para limpiar el mensaje
            patientViewModel.onSnackbarShown()
        }
    }

    Scaffold(
        topBar = {
            KetekuraTopAppBar(
                title = "Mis Atenciones",
                canNavigateBack = false,
                navController = navController,
                onRefreshClicked = { patientViewModel.loadMyPayments() }
            )
        },
        bottomBar = {
            PatientBottomNavigationBar(navController = patientNavController)
        },
        // Conectamos el SnackbarHost al Scaffold
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = patientNavController,
            startDestination = "history",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("history") { PatientHistoryScreen(patientViewModel) }
            composable("clinics") { NearbyClinicsScreen() }
        }
    }
}
