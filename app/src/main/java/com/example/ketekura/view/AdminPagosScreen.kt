package com.example.ketekura.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ketekura.model.Pago
import com.example.ketekura.viewmodel.AdminPagosViewModel

@Composable
fun AdminPagosScreen(vm: AdminPagosViewModel) {

    var rut by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Buscar Atenciones por RUT", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = rut,
            onValueChange = { rut = it },
            label = { Text("RUT del paciente") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { vm.buscarAtencionesPorRut(rut) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val lista by vm.pagos.collectAsState()

        LazyColumn {
            items(lista) { pago ->
                AtencionItem(pago) {
                    vm.pagarAtencion(
                        ateId = pago.ate_id,
                        monto = pago.monto_a_cancelar ?: 0.0
                    )
                }
            }
        }
    }
}

@Composable
fun AtencionItem(pago: Pago, onPagar: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Atención ID: ${pago.ate_id}")
            Text("Monto a pagar: ${pago.monto_a_cancelar}")
            Text("Estado: ${pago.estado}")

            if (pago.estado != "PAGADO") {
                Button(
                    onClick = onPagar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("PAGAR")
                }
            }
        }
    }
}
