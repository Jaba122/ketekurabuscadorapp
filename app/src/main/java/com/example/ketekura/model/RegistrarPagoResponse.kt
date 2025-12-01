package com.example.ketekura.model

import com.google.gson.annotations.SerializedName

// Modelo para la respuesta del endpoint /pago
data class RegistrarPagoResponse(
    val status: String,
    val message: String,
    @SerializedName("ate_id") val ateId: Int
)
