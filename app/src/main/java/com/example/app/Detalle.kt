package com.example.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
fun DetallesScreen(navController: NavController) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Botón de Volver
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF537275))
            ) {
                Text("Volver", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cuadrícula de Tarjetas
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SensorCard("Ritmo Cardíaco", "75 bpm", R.drawable.ritmo, Color(0xFFFFA07A)) { }
                    SensorCard("Temperatura", "36.6°C", R.drawable.temperatura, Color(0xFFFFD700)) { }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SensorCard("Agenda", "Ver tu agenda", R.drawable.agenda, Color(0xFF63E8F5)) {
                        navController.navigate("agenda") }
                    SensorCard("Medicamentos", "Lista de medicamentos", R.drawable.medicina, Color(0xFF0099A8)) {
                        navController.navigate("lista_medicamentos") // Navegar a la lista de medicamentos
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SensorCard("Oxigenación", "98%", R.drawable.po2, Color(0xFF25575C)) { }
                    SensorCard("Ubicación", "Ver ubicación", R.drawable.location, Color(0xFF449FA8)) {
                        navController.navigate("ubicacion") // 🔹 Navega a la pantalla del mapa
                    }
                }
            }
        }
    }
}

@Composable
fun SensorCard(title: String, value: String, icon: Int, backgroundColor: Color, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .size(150.dp)
            .clickable { onClick() }, // Hace que la tarjeta sea clickeable
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 14.sp, color = Color.White)
            Text(value, fontSize = 12.sp, color = Color.White)
        }
    }
}
