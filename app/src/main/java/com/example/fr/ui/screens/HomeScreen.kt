package com.example.fr.ui.screens

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.fr.MapView
import com.example.fr.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import java.io.IOException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(name: String, mapViewModel: MapViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(3.1390, 101.6869), 10f) // Kuala Lumpur
    }
    var showReportDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    
    // For Verification Dialog
    var selectedMarkerLocation by remember { mutableStateOf<com.example.fr.model.LocationData?>(null) }

    com.example.fr.ui.components.AppScaffold(title = "FloodRescue", navController = navController) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = "Welcome, $name!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search for a location") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        searchLocation(context, searchQuery, cameraPositionState)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Search")
                }
            }
            
            // Filter Chips
            val filters by mapViewModel.selectedFilters.collectAsState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                val filterTypes = listOf("Flood", "Shelter", "Blocked")
                filterTypes.forEach { type ->
                    FilterChip(
                        selected = filters.contains(type),
                        onClick = { mapViewModel.toggleFilter(type) },
                        label = { Text(type) },
                        leadingIcon = if (filters.contains(type)) {
                            { Icon(Icons.Default.Check, contentDescription = "Selected") }
                        } else null
                    )
                }
            }
            
            Button(
                onClick = { 
                    selectedLocation = null 
                    showReportDialog = true 
                },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Report Incident (Current View)")
            }

            MapView(
                mapViewModel = mapViewModel, 
                cameraPositionState = cameraPositionState,
                onMapLongClick = { latLng ->
                    selectedLocation = latLng
                    showReportDialog = true
                },
                onMarkerClick = { locationData ->
                    selectedMarkerLocation = locationData
                    true // Consume the click
                }
            )
        }
    }

    if (showReportDialog) {
        val targetLocation = selectedLocation ?: cameraPositionState.position.target
        ReportIncidentDialog(
            onDismiss = { showReportDialog = false },
            onSubmit = { type, description ->
                mapViewModel.reportIncident(type, description, targetLocation.latitude, targetLocation.longitude, name)
                showReportDialog = false
            }
        )
    }

    // Verification Dialog
    if (selectedMarkerLocation != null) {
        AlertDialog(
            onDismissRequest = { selectedMarkerLocation = null },
            title = { Text(selectedMarkerLocation!!.type) },
            text = {
                Column {
                    Text("Description: ${selectedMarkerLocation!!.description}")
                    Text("Reported by: ${selectedMarkerLocation!!.reportedBy}")
                    Text("Time: ${selectedMarkerLocation!!.reportedTime}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Verification Score: ${selectedMarkerLocation!!.verificationCount}", style = MaterialTheme.typography.titleMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    mapViewModel.verifyLocation(selectedMarkerLocation!!, true)
                    selectedMarkerLocation = null // Close dialog
                }) {
                    Text("Confirm (+1)")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mapViewModel.verifyLocation(selectedMarkerLocation!!, false)
                    selectedMarkerLocation = null // Close dialog
                }) {
                    Text("Reject (-1)")
                }
            }
        )
    }
}


@Composable
fun ReportIncidentDialog(onDismiss: () -> Unit, onSubmit: (String, String) -> Unit) {
    var type by remember { mutableStateOf("Flood") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val types = listOf("Flood", "Shelter", "Blocked")

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report Incident") },
        text = {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type") },
                        trailingIcon = {
                             IconButton(onClick = { expanded = true }) {
                                 Icon(Icons.Default.ArrowDropDown, contentDescription = "Drop")
                             }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        types.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t) },
                                onClick = { type = t; expanded = false }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSubmit(type, description) }) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun searchLocation(
    context: Context,
    searchQuery: String,
    cameraPositionState: CameraPositionState
) {
    try {
        val geocoder = Geocoder(context)
        val addressList = geocoder.getFromLocationName(searchQuery, 1)
        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
        }
    } catch (e: IOException) {
        // Handle exception
    }
}
