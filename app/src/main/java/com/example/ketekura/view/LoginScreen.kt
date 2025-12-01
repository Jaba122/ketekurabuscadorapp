package com.example.ketekura.view

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ketekura.viewmodel.LoginUiState
import com.example.ketekura.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    // The callback now expects a ROLE (String) on success
    onLoginSuccess: (String) -> Unit,
    // We get an instance of our ViewModel
    loginViewModel: LoginViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // We collect the state from the ViewModel
    val loginState by loginViewModel.loginState.collectAsState()

    // The LaunchedEffect will listen for a success state and navigate
    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Success) {
            onLoginSuccess((loginState as LoginUiState.Success).role)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginUiState.Loading // Disable field when loading
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            enabled = loginState !is LoginUiState.Loading // Disable field when loading
        )
        Spacer(Modifier.height(16.dp))

        // The button click now triggers the ViewModel function
        Button(
            onClick = { loginViewModel.login(username, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginUiState.Loading // Disable button when loading
        ) {
            // Show a progress indicator when loading
            if (loginState is LoginUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Entrar")
            }
        }

        // Show an error message if the state is Error
        if (loginState is LoginUiState.Error) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = (loginState as LoginUiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
