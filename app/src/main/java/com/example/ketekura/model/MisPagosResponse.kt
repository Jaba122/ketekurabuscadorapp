package com.example.ketekura.model

import com.google.gson.annotations.SerializedName

// Este modelo representa la respuesta del endpoint /mis-pagos
data class MisPagosResponse(
    @SerializedName("ate_id") val ateId: Int,
    @SerializedName("fecha_venc_pago") val fechaVencimiento: String?,
    @SerializedName("fecha_pago") val fechaPago: String?,
    @SerializedName("monto_atencion") val montoAtencion: Double?,
    @SerializedName("monto_a_cancelar") val montoACancelar: Double?,
    @SerializedName("estado") val estado: String?
)