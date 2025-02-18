package com.example.app.network

// Archivo: network/ApiClient.kt
import com.example.app.AgendaCreateRequest
import com.example.app.network.model.AgendaItem
import com.example.app.network.model.AuthResponse
import com.example.app.network.model.MedicamentoCreateRequest
import com.example.app.network.model.MedicamentoItem
import com.example.app.network.model.PacienteCuidadorRelacion
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    @GET("pacientes/{id}")
    suspend fun getPacienteById(@Path("id") id: String): Response<PacienteCuidadorRelacion>

    @GET("agenda/getAll")
    suspend fun getAllAgendas(): Response<List<AgendaItem>>

    @POST("/agenda/insert")
    suspend fun insertAgenda(@Body agenda: AgendaCreateRequest): Response<Unit>

    @GET("medicamento/getAll")
    suspend fun getAllMedicamentos(): Response<List<MedicamentoItem>>

    @POST("medicamento/insert")
    suspend fun insertMedicamento(@Body medicamento: MedicamentoCreateRequest): Response<Unit>
}



data class LoginRequest(val email: String, val password: String)

