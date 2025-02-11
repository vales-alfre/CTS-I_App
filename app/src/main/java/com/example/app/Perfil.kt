package com.example.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PerfilScreen(navController: NavController) {
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

            // Título
            Text(
                text = "¡Bienvenido, Juan!",
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
                    Text("Rol: cuidador", color = Color(0xFF537275), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Email: juan.perez2000@example.com", color = Color(0xFF537275), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Género: masculino", color = Color(0xFF537275), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Fecha de nacimiento: 1985-06-15", color = Color(0xFF537275), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Teléfono: 1234567890", color = Color(0xFF537275), fontSize = 16.sp)
                }
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
