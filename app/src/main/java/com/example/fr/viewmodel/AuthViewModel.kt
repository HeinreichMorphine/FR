package com.example.fr.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fr.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    // In-memory user storage - Companion object to persist across screens for mock purposes
    companion object {
        private val _users = MutableStateFlow<List<User>>(emptyList())
        private val _currentUser = MutableStateFlow<User?>(null)
    }
    
    val currentUser = _currentUser.asStateFlow()
    
    // Simple registration
    fun register(user: User): Boolean {
        if (_users.value.any { it.email == user.email }) {
            return false // Email already exists
        }
        _users.value = _users.value + user
        _currentUser.value = user // Auto login
        return true
    }

    // Simple login
    fun login(email: String, password: String): User? {
        val user = _users.value.find { it.email == email && it.password == password }
        if (user != null) {
            _currentUser.value = user
        }
        return user
    }
    
    // Update profile
    fun updateUserProfile(name: String, email: String, phone: String, bio: String) {
        val current = _currentUser.value ?: return
        
        // Create updated user object (keeping same password)
        val updatedUser = current.copy(
            name = name,
            email = email,
            phone = phone,
            bio = bio
        )
        
        // Update in the list
        _users.value = _users.value.map { 
            if (it.email == current.email) updatedUser else it 
        }
        
        // Update current session
        _currentUser.value = updatedUser
    }
    
    fun logout() {
        _currentUser.value = null
    }
}