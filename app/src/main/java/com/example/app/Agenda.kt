package com.example.app

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgendaScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
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
            Text("Selecciona una fecha:", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
            Button(onClick = {
                showDatePicker(context) { date ->
                    selectedDate = date
                    showDialog = true
                }
            }) {
                Text("Seleccionar Fecha")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Citas Programadas:", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))
            LazyColumn {
                items(citas) { cita ->
                    CitaCard(cita)
                }
            }
            if (showDialog) {
                AddCitaDialog(selectedDate, { titulo, fechaHora ->
                    citas.add(Cita(fechaHora, titulo))
                    showDialog = false
                    Toast.makeText(context, "Cita agregada", Toast.LENGTH_SHORT).show()
                }, { showDialog = false })
            }
        }
    }
}

fun showDatePicker(context: android.content.Context, onDateSelected: (Calendar) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            onDateSelected(selectedCalendar)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

data class Cita(val fechaHora: Date, val titulo: String)

@Composable
fun CitaCard(cita: Cita) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = cita.titulo, fontSize = 20.sp, color = Color.Black)
            Text(text = cita.fechaHora.toString(), fontSize = 16.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AddCitaDialog(
    selectedDate: Calendar,
    onAddCita: (String, Date) -> Unit,
    onDismiss: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Agregar Cita") },
        text = {
            Column {
                TextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddCita(titulo, selectedDate.time)
            }) {
                Text("Agregar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
