package com.example.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.app.network.ApiClient
import com.example.app.network.model.MedicamentoItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaMedicamentosScreen(navController: NavHostController, pacienteId: String?) {
    var medicamentos by remember { mutableStateOf<List<MedicamentoItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope() // Añade esto

    // Función para cargar medicamentos
    val loadMedicamentos = {
        scope.launch {
            try {
                val response = ApiClient.instance.getAllMedicamentos()
                if (response.isSuccessful) {
                    medicamentos = response.body() ?: emptyList()
                } else {
                    error = "Error al cargar los medicamentos"
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Cargar medicamentos cuando se inicia la pantalla
    LaunchedEffect(key1 = true) {
        loadMedicamentos()
    }

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
        when {
            isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color(0xFF0099A8))
                }
            }
            error != null -> {
                Text(
                    text = error!!,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                )
            }
            medicamentos.isEmpty() -> {
                Text(
                    text = "No hay medicamentos registrados",
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                )
            }
            else -> {
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
        }

        // Agregar el diálogo aquí
        if (showDialog) {
            RegistrarMedicamentoDialog(
                onDismiss = { showDialog = false },
                onSave = {
                    showDialog = false
                    isLoading = true
                    loadMedicamentos()
                }
            )
        }
    }
}
@Composable
fun MedicamentoCard(medicamento: MedicamentoItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF25575C)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(medicamento.nombre, fontSize = 18.sp, color = Color.White)
            Text(
                text = medicamento.descripcion,
                fontSize = 14.sp,
                color = Color(0xFF63E8F5)
            )
        }
    }
}


