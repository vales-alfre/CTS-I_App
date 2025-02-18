package com.example.app.network.model

data class PacienteCuidadorRelacion(
    val ID: Int,
    val CreatedAt: String,
    val UpdatedAt: String,
    val DeletedAt: String?,
    val PacienteID: Int,
    val CuidadorID: Int,
    val Paciente: PacienteInfo
)

data class PacienteInfo(
    val ID: Int,
    val CreatedAt: String,
    val UpdatedAt: String,
    val DeletedAt: String?,
    val UserID: Int,
    val numeroEmergencia: String,
    val User: UserInfo
)

data class UserInfo(
    val ID: Int,
    val CreatedAt: String,
    val UpdatedAt: String,
    val DeletedAt: String?,
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val birthdate: String,
    val gender: String,
    val phone: String,
    val roles: String,
    val cedula: String,
    val Cuidador: String?,
    val Paciente: String?
)