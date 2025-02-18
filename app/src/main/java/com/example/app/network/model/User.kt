package com.example.app.network.model

data class AuthResponse(val user: User?)

data class User(
    val ID: Int,
    val UpdatedAt: String,
    val DeletedAt: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val birthdate: String,
    val gender: String,
    val phone: String,
    val cedula: String,
    val roles: String
)


