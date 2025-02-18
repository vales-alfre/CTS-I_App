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
data class AgendaItem(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val hora: String,
    val estado: String,
    val paciente_id: Int,
    val paciente_nombre: String,
    val paciente_apellido: String
)
data class MedicamentoItem(
    val ID: Int,
    val CreatedAt: String,
    val UpdatedAt: String,
    val DeletedAt: String?,
    val nombre: String,
    val descripcion: String
)
data class MedicamentoCreateRequest(
    val nombre: String,
    val descripcion: String
)