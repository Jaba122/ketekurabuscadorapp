package com.example.ketekura.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagos")
data class Pago(
    @PrimaryKey
    val ate_id: Int,

    val nombre_completo: String? = null,
    val rut: String? = null,

    val fecha_venc_pago: String? = null,
    val fecha_pago: String? = null,

    val monto_atencion: Double? = null,
    val monto_a_cancelar: Double? = null,

    val obs_pago: String? = null,
    val estado: String? = null
)

