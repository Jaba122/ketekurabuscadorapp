package com.example.ketekura.network

import com.example.ketekura.model.Pago
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
    fun buscarPorAteId(@Query("ate_id") ateId: String): Call<List<Pago>>

    @GET("/")
    fun buscarPorRut(@Query("rut") rut: String): Call<List<Pago>>
}