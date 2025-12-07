package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.Pago
import com.example.ketekura.repository.PagoRepository
import com.example.ketekura.util.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PacientePagosViewModel(
    private val repo: PagoRepository
) : ViewModel() {

    private val _pagos = MutableStateFlow<List<Pago>>(emptyList())
    val pagos: StateFlow<List<Pago>> = _pagos

    fun cargarMisPagos() {
        viewModelScope.launch {
            val rut = TokenManager.getRut()
            if (rut != null) {
                _pagos.value = repo.cargarMisPagos(rut)
            }
        }
    }

    fun pagarAtencion(ateId: Int) {
        viewModelScope.launch {
            repo.registrarPago(ateId)
            cargarMisPagos() // Esto refresca la lista desde la API (y por ende, desde Oracle)
        }
    }

}
