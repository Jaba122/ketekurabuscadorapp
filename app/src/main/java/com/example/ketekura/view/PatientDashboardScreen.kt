package com.example.ketekura.view

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketekura.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDashboardScreen(
    rut: String,
    viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory(LocalContext.current.applicationContext as Application))
) {
    val isLoading = viewModel.isLoading
    val atenciones = viewModel.resultados
    val mensaje by viewModel.mensaje

    LaunchedEffect(key1 = rut) {
        viewModel.loadPatientAttentions(rut)
    }

    Scaffold(
        topBar = { KetekuraTopAppBar() } // Aquí integramos la TopAppBar
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Atenciones Por pagar Pendientes", style = MaterialTheme.typography.headlineMedium)
            Text("Ordenadas por fecha de vencimiento más próxima", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(16.dp))

            if (isLoading && atenciones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                mensaje?.let {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(
                        items = atenciones,
                        key = { it.ate_id }
                    ) { pago ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            Card(
                                Modifier
                                    .padding(vertical = 4.dp)
                                    .fillMaxWidth()
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("ATE_ID: ${pago.ate_id}")
                                    Text("Fecha de Vencimiento: ${pago.fecha_venc_pago ?: "N/A"}", style = MaterialTheme.typography.bodyLarge)
                                    Text("Monto: ${pago.monto_atencion ?: 0}")
                                    Text("Observación: ${pago.obs_pago ?: "-"}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
