package com.example.app

import HeartRateScreen
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.ui.theme.AppTheme

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
        composable("detalles") { DetallesScreen(navController) }
        composable("lista_medicamentos") { ListaMedicamentosScreen() }
        composable("ubicacion") { UbicacionScreen(navController) }
        composable("agenda") { AgendaScreen(navController) }
        composable("Monitor Cardíaco") { HeartRateScreen(navController) }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Reemplaza con tu imagen
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Login",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 24.sp,
                    color = Color(0xFF25575C) // Nurse-Enfermeria-3-hex
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = Color(0xFF537275)) }, // Nurse-Enfermeria-2-hex
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0099A8), // Nurse-Enfermeria-4-hex
                    unfocusedBorderColor = Color(0xFF537275) // Nurse-Enfermeria-2-hex
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color(0xFF537275)) }, // Nurse-Enfermeria-2-hex
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = "Toggle password visibility",
                            tint = Color(0xFF0099A8) // Nurse-Enfermeria-4-hex
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0099A8), // Nurse-Enfermeria-4-hex
                    unfocusedBorderColor = Color(0xFF537275) // Nurse-Enfermeria-2-hex
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("perfil") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0099A8)) // Nurse-Enfermeria-4-hex
            ) {
                Text(text = "Iniciar sesión", color = Color.White)
            }
        }
    }
}
