package com.example.ketekura.view

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketekura.viewmodel.AuthViewModel
import com.example.ketekura.viewmodel.LoginState

@Composable
fun PatientLoginScreen(
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(LocalContext.current.applicationContext as Application)),
    onLoginSuccess: (String) -> Unit
) {
    val loginState by authViewModel.loginState.collectAsState()
    val rut by authViewModel.patientRut.collectAsState()
    val password by authViewModel.patientPassword.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess((loginState as LoginState.Success).userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Acceso Paciente", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = rut,
            onValueChange = { authViewModel.patientRut.value = it },
            label = { Text("RUT") },
            placeholder = { Text("Ej: 12345678-9") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { authViewModel.patientPassword.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Button(
                    onClick = { authViewModel.loginPatient() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar")
                }
                if (loginState is LoginState.Error) {
                    Spacer(Modifier.height(8.dp))
                    Text((loginState as LoginState.Error).message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
