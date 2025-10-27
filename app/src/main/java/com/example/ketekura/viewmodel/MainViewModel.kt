package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.Pago
import com.example.ketekura.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.await
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class MainViewModel : ViewModel() {
    val resultados = mutableStateListOf<Pago>()
    val mensaje = mutableStateOf<String?>(null)

    fun buscar(ateId: String?, rut: String?) {
        viewModelScope.launch {
            try {
                resultados.clear()
                mensaje.value = null
                val response = when {
                    !ateId.isNullOrBlank() -> RetrofitInstance.api.buscarPorAteId(ateId).await()
                    !rut.isNullOrBlank() -> RetrofitInstance.api.buscarPorRut(rut).await()
                    else -> emptyList()
                }
                if (response.isNotEmpty()) {
                    resultados.addAll(response)
                } else {
                    mensaje.value = "No se encontraron resultados."
                }
            } catch (e: Exception) {
                mensaje.value = "Error: ${e.localizedMessage}"
            }
        }
    }
}