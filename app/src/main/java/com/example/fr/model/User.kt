package com.example.fr.model

data class User(
    val name: String,
    val email: String,
    val password: String,
    val phone: String = "",
    val bio: String = ""
)