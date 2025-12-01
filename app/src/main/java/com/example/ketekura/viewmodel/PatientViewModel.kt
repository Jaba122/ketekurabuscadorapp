package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.MisPagosResponse
import com.example.ketekura.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Sealed class para representar los estados de la UI de la pantalla del paciente
sealed class PatientUiState {
    object Loading : PatientUiState()
    data class Success(val payments: List<MisPagosResponse>) : PatientUiState()
    data class Error(val message: String) : PatientUiState()
}

class PatientViewModel : ViewModel() {

    private val _paymentsState = MutableStateFlow<PatientUiState>(PatientUiState.Loading)
    val paymentsState: StateFlow<PatientUiState> = _paymentsState

    // Función para cargar los pagos del paciente
    fun loadMyPayments() {
        viewModelScope.launch {
            _paymentsState.value = PatientUiState.Loading
            try {
                // Llamamos a la API para obtener los pagos. El token se añade automáticamente.
                val payments = RetrofitInstance.api.getMisPagos()
                _paymentsState.value = PatientUiState.Success(payments)
            } catch (e: Exception) {
                _paymentsState.value = PatientUiState.Error(e.message ?: "Error al cargar los pagos")
            }
        }
    }
}
