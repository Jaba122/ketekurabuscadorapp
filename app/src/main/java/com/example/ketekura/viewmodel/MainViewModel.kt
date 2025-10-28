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

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val resultados = mutableStateListOf<Pago>()
    val mensaje = mutableStateOf<String?>(null)

    private val _rut = MutableStateFlow("")
    val rut: StateFlow<String> = _rut.asStateFlow()

    private val _rutError = MutableStateFlow<String?>(null)
    val rutError: StateFlow<String?> = _rutError.asStateFlow()

    // Instancia del DAO
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
        // Validación
        if (!rut.isNullOrBlank()) {
            onRutChange(rut)
            if (_rutError.value != null) {
                mensaje.value = _rutError.value
                return
            }
        }

        viewModelScope.launch {
            // 1. Cargar desde la base de datos local primero
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
            } catch (e: Exception) {
                // Error al leer la cache, no hacer nada y continuar a la red
            }

            // 2. Si no hay red, mostrar mensaje y terminar
            if (!isNetworkAvailable()) {
                if (resultados.isEmpty()) {
                    mensaje.value = "No hay conexión a internet y no se encontraron datos locales."
                }
                return@launch
            }

            // 3. Buscar en la red
            try {
                val response = when {
                    !ateId.isNullOrBlank() -> RetrofitInstance.api.buscarPorAteId(ateId)
                    !rut.isNullOrBlank() -> RetrofitInstance.api.buscarPorRut(rut)
                    else -> emptyList()
                }

                if (response.isNotEmpty()) {
                    // 4. Actualizar la base de datos y la UI
                    resultados.clear()
                    resultados.addAll(response)
                    mensaje.value = null
                    // Guardar en la BD
                    pagoDao.insertAll(response)
                } else {
                    if (resultados.isEmpty()) {
                        mensaje.value = "No se encontraron resultados."
                    }
                }

            } catch (e: Exception) {
                if (resultados.isEmpty()) {
                    mensaje.value = "Error de red: ${e.localizedMessage}"
                }
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