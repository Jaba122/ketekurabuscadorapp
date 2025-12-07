package com.example.ketekura.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketekura.model.MisPagosResponse
import com.example.ketekura.viewmodel.PatientUiState
import com.example.ketekura.viewmodel.PatientViewModel

@Composable
fun PatientDashboardScreen(
    patientViewModel: PatientViewModel = viewModel()
) {
    // Nos suscribimos al estado de la UI del ViewModel
    val uiState by patientViewModel.paymentsState.collectAsState()

    // Usamos LaunchedEffect para cargar los datos solo una vez cuando la pantalla se muestra
    LaunchedEffect(Unit) {
        patientViewModel.loadMyPayments()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Mostramos diferentes vistas según el estado
        when (val state = uiState) {
            is PatientUiState.Loading -> {
                CircularProgressIndicator()
            }
            is PatientUiState.Success -> {
                if (state.payments.isEmpty()) {
                    Text("No tienes pagos pendientes.")
                } else {
                    PaymentList(payments = state.payments)
                }
            }
            is PatientUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun PaymentList(payments: List<MisPagosResponse>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(payments) { payment ->
            PaymentCard(payment = payment)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PaymentCard(payment: MisPagosResponse) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Atención ID: ${payment.ateId}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estado: ${payment.estado ?: "No especificado"}")
            Text(text = "Monto Atención: $${payment.montoAtencion ?: 0.0}")
            payment.montoACancelar?.let {
                Text(text = "Monto a Cancelar: $${it}")
            }
            payment.fechaVencimiento?.let {
                Text(text = "Vencimiento: $it")
            }
            payment.fechaPago?.let {
                Text(text = "Fecha de Pago: $it")
            }
        }
    }
}
