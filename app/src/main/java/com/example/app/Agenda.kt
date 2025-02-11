package com.example.app

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import com.kizitonwose.calendar.compose.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgendaScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val citas = remember { mutableStateListOf<Cita>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agenda de Citas", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF0099A8))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 📅 Calendario integrado en la pantalla
            Text("Selecciona una fecha:", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

            DatePicker(selectedDate, { date ->
                selectedDate = date
                showDialog = true
            })

            Spacer(modifier = Modifier.height(16.dp))

            // 📋 Lista de citas
            Text("Citas Programadas:", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

            LazyColumn {
                items(citas) { cita ->
                    CitaCard(cita)
                }
            }

            // 📝 Ventana emergente para agregar citas
            if (showDialog) {
                AddCitaDialog(selectedDate, { titulo, hora ->
                    citas.add(Cita(selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), titulo, hora))
                    showDialog = false
                    Toast.makeText(context, "Cita agregada", Toast.LENGTH_SHORT).show()
                }, { showDialog = false })
            }
        }
    }
}

// 📆 Calendario integrado en la pantalla
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.toEpochDay() * 24 * 60 * 60 * 1000
    )

    DatePicker(
        state = state,
        modifier = Modifier.fillMaxWidth().clickable {
            val millis = state.selectedDateMillis ?: return@clickable
            val date = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            onDateSelected(date)
        }
    )
}

// 📝 Composable para cada cita en la lista
@Composable
fun CitaCard(cita: Cita) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0099A8))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = cita.titulo, fontSize = 18.sp, color = Color.White)
            Text(text = "Fecha: ${cita.fecha}", fontSize = 14.sp, color = Color.White)
            Text(text = "Hora: ${cita.hora}", fontSize = 14.sp, color = Color.White)
        }
    }
}


// 📝 Diálogo para agregar una nueva cita
@Composable
fun AddCitaDialog(selectedDate: LocalDate, onConfirm: (String, String) -> Unit, onDismiss: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Agregar Cita") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título de la Cita") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora (Ejemplo: 10:00 AM)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (titulo.isNotEmpty() && hora.isNotEmpty()) {
                    onConfirm(titulo, hora)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}

// 📋 Modelo de datos para citas
data class Cita(val fecha: String, val titulo: String, val hora: String)

