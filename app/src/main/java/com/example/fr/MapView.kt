package com.example.fr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fr.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapView(
    mapViewModel: MapViewModel = viewModel(),
    cameraPositionState: CameraPositionState
) {
    val locations by mapViewModel.locations.collectAsState()
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                getCurrentLocation(context) { latLng ->
                    cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(latLng, 15f)
                }
            } else {
                // Handle permission denial
            }
        }
    )

    LaunchedEffect(Unit) {
        mapViewModel.fetchLocations()
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation(context) { latLng ->
                    cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(latLng, 15f)
                }
            }
            else -> {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getCurrentLocation(context) { latLng ->
                        cameraPositionState.position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(latLng, 15f)
                    }
                }
                else -> {
                    requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                }
            }
        }
    ) {
        locations.forEach { location ->
            Marker(
                state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                title = location.type,
                snippet = "Reported by: ${location.reportedBy} at ${location.reportedTime}"
            )
        }
    }
}

private fun getCurrentLocation(context: Context, onLocationReceived: (LatLng) -> Unit) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    try {
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                location?.let {
                    onLocationReceived(LatLng(it.latitude, it.longitude))
                }
            }
    } catch (e: SecurityException) {
        // Handle exception
    }
}