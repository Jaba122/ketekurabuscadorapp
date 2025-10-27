package com.example.ketekura.model

data class Pago(
    val ate_id: Int,
    val nombre_completo: String,
    val rut: String,
    val fecha_venc_pago: String?,
    val fecha_pago: String?,
    val monto_atencion: Double?,
    val monto_a_cancelar: Double?,
    val obs_pago: String?
)

