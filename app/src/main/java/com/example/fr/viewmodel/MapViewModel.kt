package com.example.fr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fr.model.LocationData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val _allLocations = MutableStateFlow<List<LocationData>>(emptyList())
    
    // Default filters: all types selected
    private val _selectedFilters = MutableStateFlow<Set<String>>(setOf("Flood", "Shelter", "Blocked"))
    val selectedFilters = _selectedFilters.asStateFlow()

    // Derived state for filtered locations
    private val _locations = MutableStateFlow<List<LocationData>>(emptyList())
    val locations = _locations.asStateFlow()

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }


    // Replace with your actual local IP if running on physical device
    // Use port 8000 for php artisan serve
    private val BASE_URL = "http://10.0.2.2:8000/api/"

    init {
        // Observe changes to allLocations or selectedFilters and update _locations
        viewModelScope.launch {
            kotlinx.coroutines.flow.combine(_allLocations, _selectedFilters) { all, filters ->
                if (filters.isEmpty()) {
                    emptyList() 
                } else {
                    val normalizedFilters = filters.map { it.lowercase() }.toSet()
                    all.filter { it.type.lowercase() in normalizedFilters }
                }
            }.collect { filtered ->
                _locations.value = filtered
            }
        }
        fetchLocations()
    }

    fun toggleFilter(type: String) {
        val current = _selectedFilters.value
        if (current.contains(type)) {
            _selectedFilters.value = current - type
        } else {
            _selectedFilters.value = current + type
        }
    }

    fun fetchLocations() {
        viewModelScope.launch {
            try {
                // Fetch reports
                val reports: List<LocationData> = try {
                    client.get("${BASE_URL}reports").body()
                } catch (e: Exception) { emptyList() }

                // Fetch shelters
                val shelters: List<LocationData> = try {
                    client.get("${BASE_URL}shelters").body()
                } catch (e: Exception) { emptyList() }

                // Combine both lists
                _allLocations.value = reports + shelters
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun reportIncident(type: String, description: String, lat: Double, lng: Double, user: String) {
        viewModelScope.launch {
            try {
                val newLocation = LocationData(
                    type = type,
                    latitude = lat,
                    longitude = lng,
                    reportedTime = "", // Server handles time
                    reportedBy = user,
                    description = description
                )
                
                // Post to server
                val response: LocationData = client.post("${BASE_URL}reports") {
                    contentType(ContentType.Application.Json)
                    setBody(newLocation)
                }.body()

                // Update local list with response (which should have ID and Time)
                _allLocations.value = _allLocations.value + response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun verifyLocation(location: LocationData, isUpvote: Boolean) {
        // Implementation for verification API would go here
        viewModelScope.launch {
             // Mock local update for now until API supports it
            val updatedList = _allLocations.value.map {
                if (it == location) {
                    val change = if (isUpvote) 1 else -1
                    it.copy(verificationCount = it.verificationCount + change)
                } else {
                    it
                }
            }
            _allLocations.value = updatedList
        }
    }
}