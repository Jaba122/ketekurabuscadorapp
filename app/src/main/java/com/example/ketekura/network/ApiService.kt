package com.example.ketekura.network

import com.example.ketekura.model.Pago
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("buscar")
    suspend fun buscarPorAteId(@Query("ate_id") ateId: String): List<Pago>

    @GET("buscar")
    suspend fun buscarPorRut(@Query("rut") rut: String): List<Pago>
}
