package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.Pago
import com.example.ketekura.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Sealed class para representar los estados de la búsqueda del admin
sealed class AdminSearchState {
    object Idle : AdminSearchState() // Estado inicial
    object Loading : AdminSearchState()
    data class Success(val results: List<Pago>) : AdminSearchState()
    data class Error(val message: String) : AdminSearchState()
}

class AdminViewModel : ViewModel() {

    private val _searchState = MutableStateFlow<AdminSearchState>(AdminSearchState.Idle)
    val searchState: StateFlow<AdminSearchState> = _searchState

    fun searchByRut(rut: String) {
        // Validar que el RUT no esté vacío
        if (rut.isBlank()) {
            _searchState.value = AdminSearchState.Error("El RUT no puede estar vacío.")
            return
        }

        viewModelScope.launch {
            _searchState.value = AdminSearchState.Loading
            try {
                // El token de admin se añade automáticamente por el interceptor
                val results = RetrofitInstance.api.buscarAtencionesPorRut(rut)
                _searchState.value = AdminSearchState.Success(results)
            } catch (e: Exception) {
                _searchState.value = AdminSearchState.Error(e.message ?: "Error en la búsqueda")
            }
        }
    }
}
