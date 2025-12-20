package com.example.fr.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val reportedTime: String,
    val reportedBy: String,
    val description: String = "",
    val verificationCount: Int = 0
)