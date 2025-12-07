package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.network.LoginRequest
import com.example.ketekura.network.RetrofitInstance
import com.example.ketekura.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val api = RetrofitInstance.api

    private val _estado = MutableStateFlow<String?>(null)
    val estado: StateFlow<String?> = _estado

    private val _rol = MutableStateFlow<String?>(null)
    val rol: StateFlow<String?> = _rol

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = api.login(
                    LoginRequest(username, password)
                )

                when (response.status) {
                    "ok" -> {
                        TokenManager.token = response.token
                        _rol.value = response.role
                        _estado.value = "ok"
                    }
                    "must_change_password" -> {
                        _estado.value = "must_change"
                    }
                    else -> {
                        _estado.value = "error"
                    }
                }
            } catch (e: Exception) {
                _estado.value = "error"
            }
        }
    }
}
