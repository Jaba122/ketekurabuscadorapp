package com.example.ketekura.viewmodel

import com.example.ketekura.model.MisPagosResponse
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

@ExperimentalCoroutinesApi
class PatientViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: ApiService
    private lateinit var viewModel: PatientViewModel

    @Before
    fun setUp() {
        apiService = mock()
        viewModel = PatientViewModel(apiService)
    }

    @Test
    fun `loadMyPayments success - when api returns payments - state becomes Success`() = runTest {
        // 1. Arrange
        val mockPayments = listOf(
            MisPagosResponse(1, "2024-12-31", null, 50000.0, 50000.0, "PENDIENTE"),
            MisPagosResponse(2, "2024-11-30", "2024-11-15", 25000.0, 25000.0, "PAGADO")
        )
        Mockito.`when`(apiService.getMisPagos()).thenReturn(mockPayments)

        // 2. Act
        viewModel.loadMyPayments()

        // 3. Assert
        val finalState = viewModel.paymentsState.value
        assertTrue("El estado debe ser Success", finalState is PatientUiState.Success)
        assertEquals(mockPayments, (finalState as PatientUiState.Success).payments)
    }

    @Test
    fun `loadMyPayments failure - when api throws exception - state becomes Error`() = runTest {
        // 1. Arrange
        val errorMessage = "Error de Red"
        Mockito.`when`(apiService.getMisPagos()).thenThrow(RuntimeException(errorMessage))

        // 2. Act
        viewModel.loadMyPayments()

        // 3. Assert
        val finalState = viewModel.paymentsState.value
        assertTrue("El estado debe ser Error", finalState is PatientUiState.Error)
        assertEquals(errorMessage, (finalState as PatientUiState.Error).message)
    }
}
