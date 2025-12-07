package com.example.ketekura.network

import com.example.ketekura.model.LoginRequest
import com.example.ketekura.model.LoginResponse
import com.example.ketekura.model.MisPagosResponse
import com.example.ketekura.model.Pago
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    // --- Endpoints de Paciente ---

    @GET("mis-pagos")
    suspend fun getMisPagos(
        @Query("rut") rut: String? = null // Añadido opcional por si el token falla
    ): List<MisPagosResponse>

    // CAMBIO CLAVE: Usamos Map para asegurar que mandamos "id_pago"
    // Esto evita el error donde la app mandaba "ate_id" y Python esperaba "id_pago"
    @POST("pago")
    suspend fun registrarPago(@Body body: Map<String, Any>): Map<String, String>

    // --- Endpoints de Búsqueda y Admin ---

    @GET("buscar")
    suspend fun buscarPorAteId(@Query("ate_id") ateId: String): List<Pago>

    @GET("buscar")
    suspend fun buscarPorRut(@Query("rut") rut: String): List<Pago>

    @GET("buscar-atenciones")
    suspend fun buscarAtencionesPorRut(@Query("rut") rut: String): List<Pago>

    // Para actualizar pagos como admin
    @POST("admin/update-pago")
    suspend fun updatePago(@Body body: Map<String, Any>): Map<String, Any>
}