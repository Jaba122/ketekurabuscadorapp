package com.example.ketekura.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ketekura.model.Pago
import com.example.ketekura.repository.PagoRepository
import kotlinx.coroutines.launch

class PagoViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = PagoRepository(application)

    val pagos = MutableLiveData<List<Pago>>()

    fun cargarMisPagos(rut: String? = null) {
        viewModelScope.launch {
            pagos.value = repo.cargarMisPagos(rut)
        }
    }

    fun pagar(ateId: Int) {
        viewModelScope.launch {
            repo.registrarPago(ateId)
            cargarMisPagos()
        }
    }
}
