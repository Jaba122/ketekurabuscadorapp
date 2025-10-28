package com.example.ketekura.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ketekura.db.AppDatabase
import com.example.ketekura.model.Pago
import com.example.ketekura.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val resultados = mutableStateListOf<Pago>()
    val mensaje = mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false) // Estado para el indicador de carga
        private set

    private val _rut = MutableStateFlow("")
    val rut: StateFlow<String> = _rut.asStateFlow()

    private val _rutError = MutableStateFlow<String?>(null)
    val rutError: StateFlow<String?> = _rutError.asStateFlow()

    private val pagoDao = AppDatabase.getDatabase(application).pagoDao()

    fun onRutChange(newRut: String) {
        _rut.value = newRut
        if (newRut.isBlank()) {
            _rutError.value = "El RUT no puede estar vacío"
        } else {
            _rutError.value = null
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun buscar(ateId: String?, rut: String?) {
        if (!rut.isNullOrBlank()) {
            onRutChange(rut)
            if (_rutError.value != null) {
                mensaje.value = _rutError.value
                return
            }
        }

        viewModelScope.launch {
            isLoading = true // Empezar a cargar
            mensaje.value = null
            try {
                val cachedResults = when {
                    !ateId.isNullOrBlank() -> listOfNotNull(pagoDao.getPagoByAteId(ateId.toInt()))
                    !rut.isNullOrBlank() -> pagoDao.getPagosByRut(rut)
                    else -> emptyList()
                }
                if (cachedResults.isNotEmpty()) {
                    resultados.clear()
                    resultados.addAll(cachedResults)
                }

                if (!isNetworkAvailable()) {
                    if (resultados.isEmpty()) {
                        mensaje.value = "No hay conexión a internet y no se encontraron datos locales."
                    }
                    return@launch
                }

                val response = when {
                    !ateId.isNullOrBlank() -> RetrofitInstance.api.buscarPorAteId(ateId)
                    !rut.isNullOrBlank() -> RetrofitInstance.api.buscarPorRut(rut)
                    else -> emptyList()
                }

                if (response.isNotEmpty()) {
                    resultados.clear()
                    resultados.addAll(response)
                    pagoDao.insertAll(response)
                } else {
                    if (cachedResults.isEmpty()) {
                        mensaje.value = "No se encontraron resultados."
                    }
                }
            } catch (e: Exception) {
                mensaje.value = "Error: ${e.localizedMessage}"
            } finally {
                isLoading = false // Terminar de cargar
            }
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}