package com.example.ketekura.viewmodel

import com.example.ketekura.model.Pago
import com.example.ketekura.network.ApiService
import com.example.ketekura.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.never

@ExperimentalCoroutinesApi
class AdminViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: ApiService
    private lateinit var viewModel: AdminViewModel

    @Before
    fun setUp() {
        apiService = mock()
        viewModel = AdminViewModel(apiService)
    }

    @Test
    fun `searchByRut success - when api returns results - state becomes Success`() = runTest {
        // 1. Arrange
        val rut = "12345678-9"
        val mockResults = listOf(
            Pago(1, "Juan Perez", rut, null, null, 10000.0, 10000.0, null)
        )
        Mockito.`when`(apiService.buscarAtencionesPorRut(rut)).thenReturn(mockResults)

        // 2. Act
        viewModel.searchByRut(rut)

        // 3. Assert
        val finalState = viewModel.searchState.value
        assertTrue("El estado debería ser Success", finalState is AdminSearchState.Success)
        assertEquals(mockResults, (finalState as AdminSearchState.Success).results)
    }

    @Test
    fun `searchByRut failure - when api throws exception - state becomes Error`() = runTest {
        // 1. Arrange
        val rut = "12345678-9"
        val errorMessage = "Error de Servidor"
        Mockito.`when`(apiService.buscarAtencionesPorRut(rut)).thenThrow(RuntimeException(errorMessage))

        // 2. Act
        viewModel.searchByRut(rut)

        // 3. Assert
        val finalState = viewModel.searchState.value
        assertTrue("El estado debería ser Error", finalState is AdminSearchState.Error)
        assertEquals(errorMessage, (finalState as AdminSearchState.Error).message)
    }

    @Test
    fun `searchByRut validation - when rut is blank - state becomes Error and api is not called`() = runTest {
        // 1. Arrange
        val rut = "   " // RUT en blanco

        // 2. Act
        viewModel.searchByRut(rut)

        // 3. Assert
        val finalState = viewModel.searchState.value
        assertTrue("El estado debería ser Error por validación", finalState is AdminSearchState.Error)
        assertEquals("El RUT no puede estar vacío.", (finalState as AdminSearchState.Error).message)

        // Verificamos que NUNCA se llamó a la API
        verify(apiService, never()).buscarAtencionesPorRut(Mockito.anyString())
    }
}
