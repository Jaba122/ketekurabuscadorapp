package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.MisPagosResponse
import com.example.ketekura.model.RegistrarPagoRequest
import com.example.ketekura.network.ApiService
import com.example.ketekura.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PatientUiState {
    object Loading : PatientUiState()
    data class Success(val payments: List<MisPagosResponse>) : PatientUiState()
    data class Error(val message: String) : PatientUiState()
}

class PatientViewModel(private val apiService: ApiService = RetrofitInstance.api) : ViewModel() {

    private val _paymentsState = MutableStateFlow<PatientUiState>(PatientUiState.Loading)
    val paymentsState: StateFlow<PatientUiState> = _paymentsState

    private val _isProcessingPayment = MutableStateFlow<Int?>(null)
    val isProcessingPayment: StateFlow<Int?> = _isProcessingPayment

    // Flujo para manejar los mensajes del Snackbar
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    // Función para que la UI notifique que el mensaje ya se mostró
    fun onSnackbarShown() {
        _snackbarMessage.value = null
    }

    fun loadMyPayments() {
        viewModelScope.launch {
            if (_paymentsState.value !is PatientUiState.Success) {
                _paymentsState.value = PatientUiState.Loading
            }
            try {
                val payments = apiService.getMisPagos()
                _paymentsState.value = PatientUiState.Success(payments)
            } catch (e: Exception) {
                _snackbarMessage.value = "Error al recargar los pagos."
            }
        }
    }

    fun realizarPago(atencionId: Int) {
        viewModelScope.launch {
            _isProcessingPayment.value = atencionId
            val request = RegistrarPagoRequest(idPago = atencionId)
            try {
                // Ahora 'response' es de tipo Response<RegistrarPagoResponse>
                val response = apiService.registrarPago(request)
                
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // Verificamos el cuerpo de la respuesta para la lógica de negocio
                    if (responseBody != null && responseBody.status.equals("aprobado", ignoreCase = true)) {
                        // El pago fue exitoso según el backend
                        loadMyPayments()
                    } else {
                        // El backend respondió OK, pero el pago fue rechazado
                        _snackbarMessage.value = responseBody?.message ?: "El pago fue rechazado"
                    }
                } else {
                    // La llamada HTTP falló (error 4xx o 5xx)
                    _snackbarMessage.value = "Error en el pago: ${response.code()}"
                }
            } catch (e: Exception) {
                // Error de red o excepción en la deserialización
                _snackbarMessage.value = "Error de conexión. Inténtalo de nuevo."
            } finally {
                // Nos aseguramos de detener el indicador de carga
                _isProcessingPayment.value = null
            }
        }
    }
}
