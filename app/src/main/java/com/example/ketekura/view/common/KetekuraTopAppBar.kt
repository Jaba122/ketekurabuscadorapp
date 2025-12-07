package com.example.ketekura.view.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KetekuraTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    navController: NavController,
    modifier: Modifier = Modifier,
    onRefreshClicked: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        actions = {
            if (onRefreshClicked != null) {
                IconButton(onClick = onRefreshClicked) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar"
                    )
                }
            }
            IconButton(onClick = {
                navController.navigate("login") { popUpTo(0) { inclusive = true } }
            }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Cerrar Sesión"
                )
            }
        }
    )
}
