package com.example.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Medicamento(
    val nombre: String,
    val dosis: String,
    val frecuencia: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaMedicamentosScreen() {
    val medicamentos = remember { mutableStateListOf(
        Medicamento("Paracetamol", "500 mg", "Cada 8 horas"),
        Medicamento("Ibuprofeno", "400 mg", "Cada 12 horas"),
        Medicamento("Omeprazol", "20 mg", "Antes del desayuno")
    )}

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Medicamentos", fontSize = 18.sp, color = Color.White) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF0099A8))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF0099A8),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Medicamento")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(medicamentos) { medicamento ->
                MedicamentoCard(medicamento)
            }
        }
    }

    if (showDialog) {
        RegistrarMedicamentoDialog(
            onDismiss = { showDialog = false },
            onSave = { nuevoMedicamento ->
                medicamentos.add(nuevoMedicamento)
                showDialog = false
            }
        )
    }
}

@Composable
fun MedicamentoCard(medicamento: Medicamento) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF25575C)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(medicamento.nombre, fontSize = 18.sp, color = Color.White)
            Text("Dosis: ${medicamento.dosis}", fontSize = 14.sp, color = Color(0xFF63E8F5))
            Text("Frecuencia: ${medicamento.frecuencia}", fontSize = 14.sp, color = Color(0xFF63E8F5))
        }
    }
}
