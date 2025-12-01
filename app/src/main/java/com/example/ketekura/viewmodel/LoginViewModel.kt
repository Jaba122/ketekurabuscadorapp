package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.LoginRequest
import com.example.ketekura.network.RetrofitInstance
import com.example.ketekura.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Sealed class to represent the different states of the login UI
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val role: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}


class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                val response = RetrofitInstance.api.login(LoginRequest(username, password))
                if (response.status == "ok" && response.role != null && response.token != null) {
                    // Save the token
                    TokenManager.token = response.token
                    // Notify the UI of success with the user's role
                    _loginState.value = LoginUiState.Success(response.role)
                } else {
                    // Handle other statuses from the API, like "must_change_password"
                    _loginState.value = LoginUiState.Error("Respuesta inesperada del servidor.")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
