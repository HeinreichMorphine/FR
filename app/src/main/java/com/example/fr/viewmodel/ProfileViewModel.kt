package com.example.fr.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    private val _name = MutableStateFlow("User")
    val name = _name.asStateFlow()

    private val _description = MutableStateFlow("Description goes here")
    val description = _description.asStateFlow()

    fun saveProfile(newName: String, newDescription: String) {
        _name.value = newName
        _description.value = newDescription
    }
}