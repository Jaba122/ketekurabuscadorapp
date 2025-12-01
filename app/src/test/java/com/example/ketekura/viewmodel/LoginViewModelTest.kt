package com.example.ketekura.viewmodel

import com.example.ketekura.model.LoginRequest
import com.example.ketekura.model.LoginResponse
import com.example.ketekura.network.ApiService
import com.example.ketekura.util.MainCoroutineRule
import com.example.ketekura.util.TokenManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.* // Usamos JUnit para las aserciones
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock // Mockito para simular la API

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    // Regla para ejecutar coroutines en los tests
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Mock de nuestra API Service
    private lateinit var apiService: ApiService
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        // Inicializamos el mock antes de cada test
        apiService = mock()
        viewModel = LoginViewModel(apiService) // ¡Inyectamos el mock!
        TokenManager.token = null // Nos aseguramos de que el token esté limpio
    }

    @Test
    fun `login success - when credentials are valid - state becomes Success and token is saved`() = runTest {
        // 1. Arrange (Preparar)
        val username = "testuser"
        val password = "password"
        val expectedRole = "PACIENTE"
        val expectedToken = "fake-jwt-token"
        val successResponse = LoginResponse(status = "ok", role = expectedRole, token = expectedToken)
        
        // Configuramos el mock para que devuelva una respuesta exitosa
        Mockito.`when`(apiService.login(LoginRequest(username, password))).thenReturn(successResponse)

        // 2. Act (Actuar)
        viewModel.login(username, password)

        // 3. Assert (Verificar)
        val finalState = viewModel.loginState.value
        assertTrue("El estado final debería ser Success", finalState is LoginUiState.Success)
        assertEquals(expectedRole, (finalState as LoginUiState.Success).role)
        assertEquals("El token debería haberse guardado en TokenManager", expectedToken, TokenManager.token)
    }

    @Test
    fun `login failure - when api throws exception - state becomes Error`() = runTest {
        // 1. Arrange (Preparar)
        val username = "testuser"
        val password = "password"
        val errorMessage = "Network Error"
        
        // Configuramos el mock para que lance una excepción
        Mockito.`when`(apiService.login(LoginRequest(username, password))).thenThrow(RuntimeException(errorMessage))

        // 2. Act (Actuar)
        viewModel.login(username, password)

        // 3. Assert (Verificar)
        val finalState = viewModel.loginState.value
        assertTrue("El estado final debería ser Error", finalState is LoginUiState.Error)
        assertEquals(errorMessage, (finalState as LoginUiState.Error).message)
        assertNull("El token no debería haberse guardado", TokenManager.token)
    }
}