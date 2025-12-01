package com.example.ketekura.model

import com.google.gson.annotations.SerializedName

// Modelo para la respuesta del endpoint /admin/update-pago
data class UpdatePagoResponse(
    @SerializedName("status") val status: String,
    @SerializedName("deleted_user") val deletedUser: Boolean
)
