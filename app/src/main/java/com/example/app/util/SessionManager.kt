package com.example.app.util

import com.example.app.network.model.User

object SessionManager {
    private var currentUser: User? = null

    val currentUserId: String?
        get() = currentUser?.ID?.toString()

    fun login(user: User) {
        currentUser = user
        println("Usuario guardado en SessionManager: $currentUser") // Log para debugging
    }

    fun logout() {
        currentUser = null
    }

    fun getCurrentUser(): User? = currentUser
}
