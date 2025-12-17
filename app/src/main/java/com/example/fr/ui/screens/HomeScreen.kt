package com.example.fr.ui.screens

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fr.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import java.io.IOException

@Composable
fun HomeScreen(name: String) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(1.35, 103.87), 10f)
    }

    Column {
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
        MapView(cameraPositionState = cameraPositionState)
    }
}

private fun searchLocation(
    context: Context,
    searchQuery: String,
    cameraPositionState: com.google.maps.android.compose.CameraPositionState
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
