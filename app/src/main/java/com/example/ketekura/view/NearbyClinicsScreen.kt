package com.example.ketekura.view

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.LocationServices

data class Clinic(val name: String, val latitude: Double, val longitude: Double, var distance: Float? = null)

// Lista de clínicas y hospitales de la V Región
val clinics = listOf(
    Clinic("Hospital Carlos Van Buren", -33.045, -71.620),
    Clinic("Hospital Dr. Gustavo Fricke", -33.024, -71.543),
    Clinic("Clínica Reñaca", -32.980, -71.530),
    Clinic("Clínica Ciudad del Mar", -33.010, -71.551),
    Clinic("Hospital San Martín de Quillota", -32.883, -71.246)
)

@SuppressLint("MissingPermission")
@Composable
fun NearbyClinicsScreen() {
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var isLocationLoading by remember { mutableStateOf(true) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var clinicDistances by remember { mutableStateOf<List<Clinic>>(emptyList()) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                locationPermissionGranted = true
            } else {
                isLocationLoading = false
            }
        }
    )

    LaunchedEffect(locationPermissionGranted) {
        if (locationPermissionGranted) {
            fusedLocationClient.getCurrentLocation(100, null).addOnSuccessListener { location ->
                userLocation = location
                isLocationLoading = false
            }.addOnFailureListener {
                isLocationLoading = false
            }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(userLocation) {
        userLocation?.let { loc ->
            val sortedClinics = clinics.map { clinic ->
                val results = FloatArray(1)
                Location.distanceBetween(loc.latitude, loc.longitude, clinic.latitude, clinic.longitude, results)
                clinic.copy(distance = results[0])
            }.sortedBy { it.distance }
            clinicDistances = sortedClinics
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLocationLoading) {
            CircularProgressIndicator()
        } else if (!locationPermissionGranted) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(16.dp)) {
                Text("Se necesita permiso de ubicación para encontrar clínicas cercanas.", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                    Text("Otorgar Permiso")
                }
            }
        } else if (userLocation == null) {
            Text("No se pudo obtener la ubicación. Por favor, asegúrate de tener la ubicación activada.")
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(clinicDistances) { clinic ->
                    ClinicCard(clinic)
                }
            }
        }
    }
}

@Composable
fun ClinicCard(clinic: Clinic) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.LocalHospital, contentDescription = "Clínica")
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = clinic.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                clinic.distance?.let {
                    Text(text = "${String.format("%.2f", it / 1000)} km de distancia", fontSize = 14.sp)
                }
            }
        }
    }
}
