package com.example.app.network

// Archivo: network/ApiClient.kt
import com.example.app.network.model.AuthResponse
import com.example.app.network.model.PacienteCuidadorRelacion
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://carinosaapi.onrender.com"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Tiempo de espera para establecer la conexión
        .readTimeout(30, TimeUnit.SECONDS)    // Tiempo de espera para leer datos
        .writeTimeout(30, TimeUnit.SECONDS)   // Tiempo de espera para escribir datos
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Esto es necesario
            .build()
            .create(ApiService::class.java)
    }
}

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("/pacientecuidador/getAll") // Ajusta la ruta según tu API
    suspend fun getPacientes(): Response<List<PacienteCuidadorRelacion>>

}

data class LoginRequest(val email: String, val password: String)

