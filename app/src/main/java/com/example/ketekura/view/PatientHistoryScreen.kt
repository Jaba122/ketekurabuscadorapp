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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketekura.viewmodel.MainViewModel

@Composable
fun PatientHistoryScreen(viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory(LocalContext.current.applicationContext as Application))) {
    // Recolectar el estado desde el ViewModel
    val rut by viewModel.rut.collectAsState()
    val rutError by viewModel.rutError.collectAsState()

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Buscar Historial por RUT", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = rut,
            onValueChange = { viewModel.onRutChange(it) }, // Llamar a la función del ViewModel
            label = { Text("RUT del Paciente") },
            modifier = Modifier.fillMaxWidth(),
            isError = rutError != null, // Marcar como error si existe uno
            supportingText = { // Mostrar el mensaje de error debajo del campo
                rutError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { viewModel.buscar("", rut) }, // La búsqueda usa el RUT del ViewModel
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        Spacer(Modifier.height(12.dp))

        viewModel.mensaje.value?.let {
            // Evitar mostrar el mismo error de validación dos veces
            if (it != rutError) {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(
                items = viewModel.resultados,
                key = { it.ate_id } // Clave única para un rendimiento óptimo
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
                            Text("Paciente: ${pago.nombre_completo}")
                            Text("RUT: ${pago.rut}")
                            Text("Monto: ${pago.monto_atencion ?: 0}")
                            Text("Observación: ${pago.obs_pago ?: "-"}")
                        }
                    }
                }
            }
        }
    }
}
