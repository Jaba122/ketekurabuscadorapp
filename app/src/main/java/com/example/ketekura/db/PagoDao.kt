package com.example.ketekura.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ketekura.model.Pago

@Dao
interface PagoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pagos: List<Pago>)

    @Query("SELECT * FROM pagos WHERE rut = :rut")
    suspend fun getPagosByRut(rut: String): List<Pago>

    @Query("SELECT * FROM pagos WHERE ate_id = :ateId")
    suspend fun getPagoByAteId(ateId: Int): Pago?

    @Query("DELETE FROM pagos WHERE rut = :rut")
    suspend fun deletePagosByRut(rut: String)

    @Query("DELETE FROM pagos WHERE ate_id = :ateId")
    suspend fun deletePagoByAteId(ateId: Int)
}
