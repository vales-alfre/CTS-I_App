package com.example.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

@Composable
fun DetallesScreen(navController: NavController, pacienteId: String?) {
    if (pacienteId == null) {
        Text(
            text = "Error: ID de paciente no válido",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
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
                    SensorCard(
                        "Ritmo Cardíaco",
                        "75 bpm",
                        R.drawable.ritmo,
                        Color(0xFFFFA07A)
                    ) {
                        // Asegurarse de que el ID se pase en la navegación
                        navController.navigate("Monitor Cardíaco")
                    }
                    SensorCard(
                        "Temperatura",
                        "36.6°C",
                        R.drawable.temperatura,
                        Color(0xFFFFD700)
                    ) {
                        navController.navigate("Monitor de Temperatura")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SensorCard(
                        "Agenda",
                        "Ver tu agenda",
                        R.drawable.agenda,
                        Color(0xFF63E8F5)
                    ) {
                        // Navegación a Agenda con ID
                        navController.navigate("agenda/$pacienteId")
                    }
                    SensorCard(
                        "Medicamentos",
                        "Lista de medicamentos",
                        R.drawable.medicina,
                        Color(0xFF0099A8)
                    ) {
                        // Navegación a Medicamentos con ID
                        navController.navigate("lista_medicamentos/$pacienteId")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SensorCard(
                        "Oxigenación",
                        "98%",
                        R.drawable.po2,
                        Color(0xFF25575C)
                    ) {
                        navController.navigate("Oxigenación Sanguínea")
                    }
                    SensorCard(
                        "Ubicación",
                        "Ver ubicación",
                        R.drawable.location,
                        Color(0xFF449FA8)
                    ) {
                        navController.navigate("ubicacion")
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
