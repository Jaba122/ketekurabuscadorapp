package com.example.ketekura.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ketekura.model.MisPagosResponse
import com.example.ketekura.viewmodel.PatientUiState
import com.example.ketekura.viewmodel.PatientViewModel

@Composable
fun PatientHistoryScreen(patientViewModel: PatientViewModel) {
    val uiState by patientViewModel.paymentsState.collectAsState()
    val processingPaymentId by patientViewModel.isProcessingPayment.collectAsState()

    LaunchedEffect(Unit) {
        patientViewModel.loadMyPayments()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is PatientUiState.Loading -> {
                CircularProgressIndicator()
            }
            is PatientUiState.Success -> {
                if (state.payments.isEmpty()) {
                    Text("No tienes pagos pendientes.")
                } else {
                    PaymentList(
                        payments = state.payments,
                        processingPaymentId = processingPaymentId,
                        onPagarClicked = { atencionId ->
                            patientViewModel.realizarPago(atencionId)
                        }
                    )
                }
            }
            is PatientUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun PaymentList(
    payments: List<MisPagosResponse>,
    processingPaymentId: Int?,
    onPagarClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(payments) { payment ->
            PaymentCard(
                payment = payment,
                isProcessing = processingPaymentId == payment.ateId,
                onPagarClicked = onPagarClicked
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PaymentCard(
    payment: MisPagosResponse,
    isProcessing: Boolean,
    onPagarClicked: (Int) -> Unit
) {
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

            if (payment.estado == "PENDIENTE") {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.align(Alignment.End),
                    contentAlignment = Alignment.Center
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Button(onClick = { onPagarClicked(payment.ateId) }) {
                            Text("Pagar")
                        }
                    }
                }
            }
        }
    }
}
