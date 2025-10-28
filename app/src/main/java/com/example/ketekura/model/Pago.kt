package com.example.ketekura.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagos")
data class Pago(
    @PrimaryKey
    val ate_id: Int,
    val nombre_completo: String,
    val rut: String,
    val fecha_venc_pago: String?,
    val fecha_pago: String?,
    val monto_atencion: Double?,
    val monto_a_cancelar: Double?,
    val obs_pago: String?
)
