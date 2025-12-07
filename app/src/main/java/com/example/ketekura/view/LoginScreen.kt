package com.example.ketekura.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ketekura.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    vm: LoginViewModel,
    onAdminLogin: () -> Unit,
    onPacienteLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val estado by vm.estado.collectAsState()
    val rol by vm.rol.collectAsState()

    LaunchedEffect(estado, rol) {
        if (estado == "ok") {
            if ("ADMIN".equals(rol, ignoreCase = true)) {
                onAdminLogin()
            } else {
                onPacienteLogin()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { vm.login(username, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        if (estado == "error") {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Usuario o contraseña incorrectos", color = MaterialTheme.colorScheme.error)
        }

        if (estado == "must_change") {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Debes cambiar tu contraseña", color = MaterialTheme.colorScheme.primary)
        }
    }
}
