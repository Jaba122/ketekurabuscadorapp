package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.Pago
import com.example.ketekura.network.ApiService
import com.example.ketekura.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminSearchState {
    object Idle : AdminSearchState()
    object Loading : AdminSearchState()
    data class Success(val results: List<Pago>) : AdminSearchState()
    data class Error(val message: String) : AdminSearchState()
}

// AHORA el ViewModel recibe la ApiService en su constructor
class AdminViewModel(private val apiService: ApiService = RetrofitInstance.api) : ViewModel() {

    private val _searchState = MutableStateFlow<AdminSearchState>(AdminSearchState.Idle)
    val searchState: StateFlow<AdminSearchState> = _searchState

    fun searchByRut(rut: String) {
        if (rut.isBlank()) {
            _searchState.value = AdminSearchState.Error("El RUT no puede estar vacío.")
            return
        }

        viewModelScope.launch {
            _searchState.value = AdminSearchState.Loading
            try {
                // Usamos la instancia de apiService inyectada
                val results = apiService.buscarAtencionesPorRut(rut)
                _searchState.value = AdminSearchState.Success(results)
            } catch (e: Exception) {
                _searchState.value = AdminSearchState.Error(e.message ?: "Error en la búsqueda")
            }
        }
    }
}
