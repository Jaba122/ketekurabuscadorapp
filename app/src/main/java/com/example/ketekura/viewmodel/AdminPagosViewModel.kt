package com.example.ketekura.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.Pago
import com.example.ketekura.repository.PagoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminPagosViewModel(
    private val repository: PagoRepository
) : ViewModel() {

    private val _pagos = MutableStateFlow<List<Pago>>(emptyList())
    val pagos: StateFlow<List<Pago>> = _pagos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun buscarAtencionesPorRut(rut: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val resultado = repository.buscarPagos(null, rut)
                _pagos.value = resultado
            } catch (e: Exception) {
                _mensaje.value = "Error al buscar atenciones"
            } finally {
                _loading.value = false
            }
        }
    }

    fun pagarAtencion(ateId: Int, monto: Double) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val ok = repository.adminActualizarPago(
                    ateId = ateId,
                    monto = monto,
                    obs = "Pagado desde app por admin"
                )
                if (ok) {
                    _mensaje.value = "Pago realizado"
                } else {
                    _mensaje.value = "Error al pagar"
                }
            } finally {
                _loading.value = false
            }
        }
    }
}
