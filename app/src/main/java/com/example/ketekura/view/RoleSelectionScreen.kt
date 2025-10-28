package com.example.ketekura.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RoleSelectionScreen(
    onNavigateToAdminLogin: () -> Unit,
    onNavigateToPatientLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bienvenido a Ketekura", style = MaterialTheme.typography.headlineLarge)
        Text("Por favor, selecciona tu rol", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))

        Button(onClick = onNavigateToAdminLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Soy Administrador")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onNavigateToPatientLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Soy Paciente")
        }
    }
}
