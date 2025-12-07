package com.example.ketekura.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ketekura.model.Pago
import com.example.ketekura.viewmodel.PacientePagosViewModel

@Composable
fun PacientePagosScreen(vm: PacientePagosViewModel) {

    val pagos by vm.pagos.collectAsState()

    LaunchedEffect(Unit) {
        vm.cargarMisPagos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mis atenciones",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(pagos) { pago ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text("ID Atención: ${pago.ate_id}")
                        Text("Monto: $${pago.monto_a_cancelar}")
                        Text("Estado: ${pago.estado}")

                        if (pago.estado == "PENDIENTE") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    vm.pagarAtencion(pago.ate_id)

                                }
                            ) {
                                Text("Pagar")
                            }
                        }
                    }
                }
            }
        }
    }
}
