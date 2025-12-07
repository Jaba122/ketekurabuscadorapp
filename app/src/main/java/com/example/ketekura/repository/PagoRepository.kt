package com.example.ketekura.repository

import android.content.Context
import com.example.ketekura.network.RetrofitInstance

class PagoRepository(context: Context) {

    private val api = RetrofitInstance.api

    suspend fun cargarMisPagos(rut: String? = null) =
        api.getMisPagos(rut)

    suspend fun buscarPagos(ateId: Int?, rut: String?) =
        api.buscarPagos(ateId, rut)

    suspend fun registrarPago(ateId: Int): Boolean {
        return try {
            api.registrarPago(
                mapOf("id_pago" to ateId, "obs" to "Pago móvil")
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    suspend fun adminActualizarPago(
        ateId: Int,
        monto: Double?,
        obs: String?
    ): Boolean {
        return try {
            api.adminUpdatePago(
                com.example.ketekura.network.AdminUpdatePagoRequest(
                    ateId, monto, obs
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}
