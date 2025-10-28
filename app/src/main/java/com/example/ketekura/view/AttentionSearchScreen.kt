package com.example.ketekura.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketekura.viewmodel.MainViewModel

@Composable
fun AttentionSearchScreen(viewModel: MainViewModel = viewModel()) {
    var ateId by remember { mutableStateOf("") }

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Buscar Pago por ATE_ID", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = ateId,
            onValueChange = { ateId = it },
            label = { Text("ATE_ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { viewModel.buscar(ateId, "") }, // Search only by ateId
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        Spacer(Modifier.height(12.dp))

        viewModel.mensaje.value?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(viewModel.resultados.size) { index ->
                val pago = viewModel.resultados[index]
                Card(
                    Modifier
                        .padding(8.dp)
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
