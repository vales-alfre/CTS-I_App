package com.example.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app.util.SessionManager

@Composable
fun PerfilScreen(navController: NavController) {
    val currentUser = SessionManager.getCurrentUser()
    Scaffold(
        bottomBar = { BottomNavBar(navController) } // La barra de navegación solo aparece en Perfil
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            currentUser?.let { user ->
                Text(
                    text = "¡Bienvenido, ${user.firstname} ${user.lastname}!",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp,
                color = Color(0xFF25575C)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de información
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Rol: ${user.roles}", color = Color(0xFF537275), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Email: ${user.email}", color = Color(0xFF537275), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Género: ${user.gender}", color = Color(0xFF537275), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fecha de nacimiento: ${user.birthdate}", color = Color(0xFF537275), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Teléfono: 1234567890", color = Color(0xFF537275), fontSize = 16.sp)
                }
            }
            } ?: run {
                Text(
                    text = "No se encontró información del usuario",
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Cerrar Sesión
            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true } // Elimina el historial de navegación
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0099A8))
            ) {
                Text("Cerrar Sesión", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
