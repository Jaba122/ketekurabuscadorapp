package com.example.ketekura.model

import com.google.gson.annotations.SerializedName

// Modelo para la petición del endpoint /pago
data class RegistrarPagoRequest(
    @SerializedName("id_pago") val idPago: Int,
    @SerializedName("monto") val monto: Double,
    @SerializedName("obs") val observacion: String? = null
)
