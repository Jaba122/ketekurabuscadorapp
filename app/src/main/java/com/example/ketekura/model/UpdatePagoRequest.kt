package com.example.ketekura.model

import com.google.gson.annotations.SerializedName

// Modelo para la petición del endpoint /admin/update-pago
data class UpdatePagoRequest(
    @SerializedName("ate_id") val ateId: Int,
    @SerializedName("monto") val monto: Double? = null, // Es opcional
    @SerializedName("obs") val obs: String? = null // Es opcional
)
