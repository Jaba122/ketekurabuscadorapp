package com.example.ketekura.network

import com.example.ketekura.model.LoginRequest
import com.example.ketekura.model.LoginResponse
import com.example.ketekura.model.MisPagosResponse
import com.example.ketekura.model.Pago
import com.example.ketekura.model.RegistrarPagoRequest
import com.example.ketekura.model.RegistrarPagoResponse
import com.example.ketekura.model.UpdatePagoRequest
import com.example.ketekura.model.UpdatePagoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // --- Endpoints de Login y Búsqueda (ya existentes) ---

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("buscar")
    suspend fun buscarPorAteId(@Query("ate_id") ateId: String): List<Pago>

    @GET("buscar")
    suspend fun buscarPorRut(@Query("rut") rut: String): List<Pago>

    // --- Endpoints de Paciente ---

    @GET("mis-pagos")
    suspend fun getMisPagos(): List<MisPagosResponse>

    @POST("pago")
    suspend fun registrarPago(@Body request: RegistrarPagoRequest): RegistrarPagoResponse

    // --- Endpoints de Administrador ---

    @GET("buscar-atenciones")
    suspend fun buscarAtencionesPorRut(@Query("rut") rut: String): List<Pago>

    // Nota: Tu API devuelve un mensaje simple. Si en el futuro devuelve una lista de pagos,
    // el tipo de retorno debería ser List<Pago>.
    @GET("admin/pagos")
    suspend fun getAdminPagos(): Map<String, String>

    @POST("admin/update-pago")
    suspend fun updatePago(@Body request: UpdatePagoRequest): UpdatePagoResponse
}
