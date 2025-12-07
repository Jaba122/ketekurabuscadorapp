package com.example.ketekura.util

import com.auth0.android.jwt.JWT

object TokenManager {
    var token: String? = null

    // Esta función es VITAL para saber qué pagos cargar
    fun getRut(): String? {
        return token?.let {
            try {
                val jwt = JWT(it)
                val rut = jwt.getClaim("sub").asString()
                // Validación extra por si Python devuelve "None" como string
                if (rut.isNullOrEmpty() || rut == "None") null else rut
            } catch (e: Exception) {
                null
            }
        }
    }
}