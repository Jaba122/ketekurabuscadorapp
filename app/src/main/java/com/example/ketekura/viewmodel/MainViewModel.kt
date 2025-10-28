package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.Pago
import com.example.ketekura.network.RetrofitInstance
import kotlinx.coroutines.launch
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

                // ✅ Retrofit ya soporta funciones suspend, no hace falta .await()
                val response = when {
                    !ateId.isNullOrBlank() -> RetrofitInstance.api.buscarPorAteId(ateId)
                    !rut.isNullOrBlank() -> RetrofitInstance.api.buscarPorRut(rut)
                    else -> emptyList()
                }

                // ✅ Kotlin sabe qué hacer con emptyList() (es del stdlib)
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