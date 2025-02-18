package com.example.app

import HeartRateScreen
import OxygenScreen
import TemperatureScreen
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.network.ApiClient
import com.example.app.network.LoginRequest
import com.example.app.ui.theme.AppTheme
import com.example.app.util.SessionManager
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController) }
        composable("perfil") { PerfilScreen(navController) }
        composable("lista") { ListaScreen(navController) }
        composable("detalles/{pacienteId}") { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getString("pacienteId")
            DetallesScreen(navController, pacienteId)}
        composable("lista_medicamentos") { ListaMedicamentosScreen() }
        composable("ubicacion") { UbicacionScreen(navController) }
        composable("agenda") { AgendaScreen(navController) }
        composable("Monitor Cardíaco") { HeartRateScreen(navController) }
        composable("Monitor de Temperatura") { TemperatureScreen(navController)}
        composable("Oxigenación Sanguínea") { OxygenScreen(navController)}

    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Login",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 24.sp,
                    color = Color(0xFF25575C)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null // Limpiar error al editar
                },
                label = { Text("Correo electrónico", color = Color(0xFF537275)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (emailError != null) Color.Red else Color(0xFF0099A8),
                    unfocusedBorderColor = if (emailError != null) Color.Red else Color(0xFF537275)
                ),
                supportingText = {
                    emailError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null // Limpiar error al editar
                },
                label = { Text("Contraseña", color = Color(0xFF537275)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.baseline_visibility_24
                                else R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = "Toggle password visibility",
                            tint = Color(0xFF0099A8)
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (passwordError != null) Color.Red else Color(0xFF0099A8),
                    unfocusedBorderColor = if (passwordError != null) Color.Red else Color(
                        0xFF537275
                    )
                ),
                supportingText = {
                    passwordError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Validaciones locales primero
                    emailError = when {
                        email.isEmpty() -> "El correo electrónico es requerido"
                        !isValidEmail(email) -> "Formato de correo electrónico inválido"
                        else -> null
                    }

                    passwordError = when {
                        password.isEmpty() -> "La contraseña es requerida"
                        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                        else -> null
                    }

                    if (emailError == null && passwordError == null) {
                        isLoading = true
                        coroutineScope.launch {
                            try {
                                val response = ApiClient.instance.login(
                                    LoginRequest(email, password)
                                )
                                if (response.isSuccessful) {
                                    val authResponse = response.body() // Retrofit deserializa automáticamente
                                    authResponse?.user?.let { user -> SessionManager.login(user)
                                        println("El ID del usuario es: ${SessionManager.getCurrentUser()}")
                                    navController.navigate("perfil")
                                    } ?: run {
                                        passwordError = "Error: No se pudo obtener la información del usuario"
                                    }
                                } else {
                                    passwordError = "Error en la autenticación: ${response.code()}"
                                }
                            } catch (e: Exception) {
                                // Manejar errores de red
                                passwordError = "Error de conexión: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0099A8)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(text = "Iniciar sesión", color = Color.White)
                }
            }
        }
    }
}
