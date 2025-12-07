package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.MisPagosResponse
import com.example.ketekura.network.ApiService
import com.example.ketekura.network.RetrofitInstance
import com.example.ketekura.util.TokenManager // Importante
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

    fun loadMyPayments() {
        viewModelScope.launch {
            _paymentsState.value = PatientUiState.Loading
            try {
                // Obtenemos el RUT del token decodificado
                val rut = TokenManager.getRut()

                // Llamamos a la API. Si el RUT es nulo, la API usará el del token en el header (si está configurado así en Python)
                val payments = apiService.getMisPagos(rut)
                _paymentsState.value = PatientUiState.Success(payments)
            } catch (e: Exception) {
                _paymentsState.value = PatientUiState.Error(e.message ?: "Error al cargar los pagos")
            }
        }
    }

    // Nueva función para realizar el pago
    fun pagarAtencion(ateId: Int) {
        viewModelScope.launch {
            try {
                // Enviamos EXACTAMENTE lo que pide app.py: "id_pago"
                val body = mapOf(
                    "id_pago" to ateId,
                    "obs" to "Pago desde App Android"
                )

                apiService.registrarPago(body)

                // Si sale bien, recargamos la lista INMEDIATAMENTE desde el servidor
                loadMyPayments()

            } catch (e: Exception) {
                _paymentsState.value = PatientUiState.Error("Error al procesar el pago: ${e.message}")
            }
        }
    }
}