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
            json()
        }
    }

    init {
        // Observe changes to allLocations or selectedFilters and update _locations
        viewModelScope.launch {
            kotlinx.coroutines.flow.combine(_allLocations, _selectedFilters) { all, filters ->
                if (filters.isEmpty()) {
                    emptyList() // Or all if you prefer showing everything when nothing selected, but usually empty implies no selection
                } else {
                    all.filter { it.type in filters }
                }
            }.collect { filtered ->
                _locations.value = filtered
            }
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
                LocationData("Flood", 3.140853, 101.693207, "2024-12-20 10:00", "Admin", "Flooding at Dataran Merdeka"),
                LocationData("Shelter", 3.1390, 101.6869, "2024-12-20 09:00", "System", "Shelter at National Mosque"),
                LocationData("Blocked", 3.1579, 101.7116, "2024-12-20 11:30", "User1", "Road blocked near KLCC")
            )
            _allLocations.value = fetched
        }
    }

    fun reportIncident(type: String, description: String, lat: Double, lng: Double, user: String) {
        val newLocation = LocationData(
            type = type,
            latitude = lat,
            longitude = lng,
            reportedTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date()),
            reportedBy = user,
            description = description
        )
        _allLocations.value = _allLocations.value + newLocation
    }

    fun verifyLocation(location: LocationData, isUpvote: Boolean) {
        val updatedList = _allLocations.value.map {
            if (it == location) {
                // In a real app, we would send this to the server.
                // Here we just increment/decrement locally for the demo.
                val change = if (isUpvote) 1 else -1
                it.copy(verificationCount = it.verificationCount + change)
            } else {
                it
            }
        }
        _allLocations.value = updatedList
    }
}