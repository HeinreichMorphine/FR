package com.example.fr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fr.model.LocationData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val _locations = MutableStateFlow<List<LocationData>>(emptyList())
    val locations = _locations.asStateFlow()

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    fun fetchLocations() {
        viewModelScope.launch {
            try {
                val response = client.get("YOUR_SERVER_URL_HERE")
                _locations.value = response.body()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}