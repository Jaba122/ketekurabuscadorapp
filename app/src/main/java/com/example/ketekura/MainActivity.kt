package com.example.ketekura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*

import com.example.ketekura.repository.PagoRepository
import com.example.ketekura.view.AdminPagosScreen
import com.example.ketekura.view.LoginScreen
import com.example.ketekura.view.PacientePagosScreen
import com.example.ketekura.viewmodel.AdminPagosViewModel
import com.example.ketekura.viewmodel.LoginViewModel
import com.example.ketekura.viewmodel.PacientePagosViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pagoRepository = PagoRepository(this)
        val adminVm = AdminPagosViewModel(pagoRepository)
        val loginVm = LoginViewModel()

        setContent {
            var pantalla by remember { mutableStateOf("login") }

            when (pantalla) {

                "login" -> LoginScreen(
                    vm = loginVm,
                    onAdminLogin = {
                        pantalla = "admin"
                    },
                    onPacienteLogin = {
                        pantalla = "paciente"
                    }
                )

                "admin" -> AdminPagosScreen(adminVm)

                "paciente" -> {
                    val pacienteVm = PacientePagosViewModel(pagoRepository)
                    PacientePagosScreen(pacienteVm)
                }
            }

        }
    }
}

