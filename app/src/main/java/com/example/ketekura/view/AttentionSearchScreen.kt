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

@Composable
fun AttentionSearchScreen(viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory(LocalContext.current.applicationContext as Application))) {
    var ateId by remember { mutableStateOf("") }
    val isLoading = viewModel.isLoading

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
            onClick = { viewModel.buscar(ateId, "") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            viewModel.mensaje.value?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                items(
                    items = viewModel.resultados,
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
}
