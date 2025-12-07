package com.example.ketekura.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketekura.model.Pago
import com.example.ketekura.viewmodel.AdminSearchState
import com.example.ketekura.viewmodel.AdminViewModel

@Composable
fun AdminDashboard(
    adminViewModel: AdminViewModel = viewModel()
) {
    var rut by remember { mutableStateOf("") }
    val searchState by adminViewModel.searchState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Búsqueda de Atenciones", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        // Campo de búsqueda y botón
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("Ingresar RUT del paciente") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = { adminViewModel.searchByRut(rut) }) {
                Text("Buscar")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Sección de resultados
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = searchState) {
                is AdminSearchState.Idle -> {
                    Text("Ingrese un RUT para comenzar la búsqueda.")
                }
                is AdminSearchState.Loading -> {
                    CircularProgressIndicator()
                }
                is AdminSearchState.Success -> {
                    if (state.results.isEmpty()) {
                        Text("No se encontraron atenciones para el RUT ingresado.")
                    } else {
                        AttentionList(attentions = state.results)
                    }
                }
                is AdminSearchState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun AttentionList(attentions: List<Pago>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(attentions) { attention ->
            AttentionCard(attention = attention)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AttentionCard(attention: Pago) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Paciente: ${attention.nombre_completo}", style = MaterialTheme.typography.titleMedium)
            Text("RUT: ${attention.rut}")
            Text("Atención ID: ${attention.ate_id}")
            Spacer(Modifier.height(8.dp))
            Text("Monto: $${attention.monto_atencion ?: 0.0}")
            Text("Observación: ${attention.obs_pago ?: "Sin observaciones"}")
            attention.fecha_pago?.let {
                Text("Fecha de Pago: $it")
            } ?: Text("Estado: PENDIENTE")
        }
    }
}

