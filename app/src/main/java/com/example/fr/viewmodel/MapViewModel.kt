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

    // Shared state across ViewModel instances for mock purposes
    companion object {
        private val _allLocations = MutableStateFlow<List<LocationData>>(emptyList())
        private val _selectedFilters = MutableStateFlow<Set<String>>(setOf("Flood", "Shelter", "Blocked"))
    }
    
    val selectedFilters = _selectedFilters.asStateFlow()

    // Derived state for filtered locations (local to this instance, but derived from shared)
    private val _locations = MutableStateFlow<List<LocationData>>(emptyList())
    val locations = _locations.asStateFlow()

    // private val client ... (Removed/Unused)

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
        
        // Only fetch if empty to avoid overwriting user reports on navigation
        if (_allLocations.value.isEmpty()) {
            fetchLocations()
        }
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
            // Mock data for Malaysia (Kuala Lumpur area)
            val fetched = listOf(
                LocationData(type = "Flood", latitude = 3.140853, longitude = 101.693207, reportedTime = "2024-12-20 10:00", reportedBy = "Admin", description = "Flooding at Dataran Merdeka"),
                LocationData(type = "Shelter", latitude = 3.1390, longitude = 101.6869, reportedTime = "2024-12-20 09:00", reportedBy = "System", description = "Shelter at National Mosque"),
                LocationData(type = "Blocked", latitude = 3.1579, longitude = 101.7116, reportedTime = "2024-12-20 11:30", reportedBy = "User1", description = "Road blocked near KLCC")
            )
            _allLocations.value = fetched
        }
    }

    fun reportIncident(type: String, description: String, lat: Double, lng: Double, user: String) {
        viewModelScope.launch {
            // Mocking the server response by creating a local object
            val newLocation = LocationData(
                type = type,
                latitude = lat,
                longitude = lng,
                reportedTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
                reportedBy = user,
                description = description
            )
            
            // Add to local list directly
            _allLocations.value = _allLocations.value + newLocation
        }
    }

    fun verifyLocation(location: LocationData, isUpvote: Boolean) {
        viewModelScope.launch {
             // Mock local update
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
