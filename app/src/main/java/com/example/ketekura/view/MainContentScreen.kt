package com.example.ketekura.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContentScreen(
    username: String,
    onNavigateToAttentionSearch: () -> Unit,
    onNavigateToPatientHistory: () -> Unit
) {
    Scaffold(
        topBar = { KetekuraTopAppBar() } // Aquí integramos la TopAppBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp), // Añadimos un padding adicional para el contenido
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Perfil de Usuario", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("Usuario: $username")
                }
            }

            Button(
                onClick = onNavigateToAttentionSearch,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar Atenciones por ATE_ID")
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onNavigateToPatientHistory,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar Historial de Paciente por RUT")
            }
        }
    }
}
