package com.example.ketekura.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ketekura.db.AppDatabase
import com.example.ketekura.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    // Estado para el login del paciente
    val patientRut = MutableStateFlow("")
    val patientPassword = MutableStateFlow("")

    fun loginPatient() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            // Para probar, vamos a insertar un usuario si no existe
            val testUser = userDao.findByRut("12345678-9")
            if (testUser == null) {
                userDao.insert(User(rut = "12345678-9", password = "password"))
            }

            val user = userDao.findByRut(patientRut.value)
            if (user != null && user.password == patientPassword.value) {
                _loginState.value = LoginState.Success(user.rut)
            } else {
                _loginState.value = LoginState.Error("RUT o contraseña incorrectos")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    // Factory para el ViewModel
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

// Estados del proceso de login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userId: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
