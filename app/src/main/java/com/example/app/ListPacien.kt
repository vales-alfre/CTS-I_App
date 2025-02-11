package com.example.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ListaScreen(navController: NavController) {
    val peopleList = listOf(
        Person("Valeska chica", "Femenino", 38),
        Person("Juan Pérez", "Masculino", 45),
        Person("María López", "Femenino", 29),
        Person("Carlos Méndez", "Masculino", 50),
        Person("Ana Gómez", "Femenino", 33)
    )

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(peopleList) { person ->
                    CardItem(
                        name = person.name,
                        gender = person.gender,
                        age = person.age,
                        modifier = Modifier.clickable {
                            navController.navigate("detalles") // Navega a `DetallesScreen 1`
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CardItem(name: String, gender: String, age: Int, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF449FA8) // Nurse-Enfermeria-1-hex (Turquesa)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Género: $gender",
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = "Edad: $age años",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

data class Person(val name: String, val gender: String, val age: Int)
