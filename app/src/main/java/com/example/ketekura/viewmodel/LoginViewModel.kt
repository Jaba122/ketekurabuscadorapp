package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.LoginRequest
import com.example.ketekura.network.ApiService
import com.example.ketekura.network.RetrofitInstance
import com.example.ketekura.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val role: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

// AHORA el ViewModel recibe la ApiService en su constructor, con un valor por defecto
// para no romper el código de producción.
class LoginViewModel(private val apiService: ApiService = RetrofitInstance.api) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                // Usamos la instancia de apiService inyectada en lugar del singleton
                val response = apiService.login(LoginRequest(username, password))
                if (response.status == "ok" && response.role != null && response.token != null) {
                    TokenManager.token = response.token
                    _loginState.value = LoginUiState.Success(response.role)
                } else {
                    _loginState.value = LoginUiState.Error("Respuesta inesperada del servidor.")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
