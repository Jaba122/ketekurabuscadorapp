package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.MisPagosResponse
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

// AHORA el ViewModel recibe la ApiService en su constructor
class PatientViewModel(private val apiService: ApiService = RetrofitInstance.api) : ViewModel() {

    private val _paymentsState = MutableStateFlow<PatientUiState>(PatientUiState.Loading)
    val paymentsState: StateFlow<PatientUiState> = _paymentsState

    fun loadMyPayments() {
        viewModelScope.launch {
            _paymentsState.value = PatientUiState.Loading
            try {
                // Usamos la instancia de apiService inyectada
                val payments = apiService.getMisPagos()
                _paymentsState.value = PatientUiState.Success(payments)
            } catch (e: Exception) {
                _paymentsState.value = PatientUiState.Error(e.message ?: "Error al cargar los pagos")
            }
        }
    }
}
