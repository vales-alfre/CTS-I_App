package com.example.app

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app.network.ApiClient
import com.example.app.network.model.AgendaItem
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class AgendaCreateRequest(
    val PacienteID: Int,
    val nombre: String,
    val descripcion: String,
    val Fecha: String,
    val Hora: String
)

fun parseDateTime(dateStr: String): LocalDate? {
    return try {
        Instant.parse(dateStr)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } catch (e: Exception) {
        try {
            LocalDateTime.parse(dateStr.removeSuffix("Z"))
                .toLocalDate()
        } catch (e: Exception) {
            null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AgendaScreen(navController: NavController, pacienteId: String?) {
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val state = rememberCalendarState(
        startMonth = currentMonth.minusMonths(12),
        endMonth = currentMonth.plusMonths(12),
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = DayOfWeek.MONDAY
    )

    var selectedDate by remember { mutableStateOf(currentDate) }
    var showDialog by remember { mutableStateOf(false) }
    var agendas by remember { mutableStateOf<List<AgendaItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pacienteId) {
        if (pacienteId == null) {
            error = "ID de paciente no válido"
            isLoading = false
            return@LaunchedEffect
        }

        try {
            val response = ApiClient.instance.getAllAgendas()
            if (response.isSuccessful) {
                // Filtrar solo por pacienteId, sin filtrar por fecha
                agendas = response.body()?.filter {
                    it.paciente_id.toString() == pacienteId &&
                            it.estado.lowercase() == "pendiente"
                }?.sortedBy { it.fecha } ?: emptyList()
            } else {
                error = "Error al cargar las agendas"
            }
        } catch (e: Exception) {
            error = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

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
        ) {
            // Sección del calendario para registrar citas
            Text(
                "Seleccionar fecha para nueva cita:",
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            HorizontalCalendar(
                state = state,
                dayContent = { day ->
                    Day(
                        day = day,
                        isSelected = selectedDate == day.date,
                        hasAgenda = false  // Ya no necesitamos mostrar días con agenda
                    ) {
                        selectedDate = day.date
                        showDialog = true
                    }
                },
                monthHeader = { month ->
                    MonthHeader(month)
                },
                modifier = Modifier
                    .height(320.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de citas pendientes
            Text(
                "Citas Pendientes:",
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            } else if (error != null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                if (agendas.isEmpty()) {
                    Text(
                        text = "No hay citas pendientes.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn {
                        items(agendas) { agenda ->
                            AgendaCard(agenda)
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AddCitaDialog(
                selectedDate = selectedDate,
                pacienteId = pacienteId ?: "",
                onAddCita = { agendaRequest ->
                    scope.launch {
                        try {
                            val response = ApiClient.instance.insertAgenda(agendaRequest)
                            if (response.isSuccessful) {
                                val updatedResponse = ApiClient.instance.getAllAgendas()
                                if (updatedResponse.isSuccessful) {
                                    agendas = updatedResponse.body()?.filter {
                                        it.paciente_id.toString() == pacienteId &&
                                                it.estado.lowercase() == "pendiente"
                                    }?.sortedBy { it.fecha } ?: emptyList()
                                }
                            } else {
                                error = "Error al registrar la agenda"
                            }
                        } catch (e: Exception) {
                            error = "Error: ${e.message}"
                        } finally {
                            showDialog = false
                        }
                    }
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}
@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    hasAgenda: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .background(
                color = when {
                    isSelected -> Color(0xFF0099A8)
                    hasAgenda -> Color(0xFFE0F7FA)
                    else -> Color.Transparent
                },
                shape = CircleShape
            )
            .clickable(enabled = day.position == DayPosition.MonthDate) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                isSelected -> Color.White
                hasAgenda -> Color(0xFF0099A8)
                day.position == DayPosition.MonthDate -> Color.Black
                else -> Color.LightGray
            }
        )
    }
}

@Composable
private fun MonthHeader(month: CalendarMonth) {
    val formatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy") }
    Text(
        text = month.yearMonth.format(formatter).replaceFirstChar { it.titlecase() },
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun AgendaCard(agenda: AgendaItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = agenda.nombre,
                fontSize = 20.sp,
                color = Color(0xFF0099A8),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = agenda.descripcion,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Fecha: ${formatearFecha(agenda.fecha)}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "Hora: ${formatearHora(agenda.hora)}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "Estado: ${agenda.estado}",
                fontSize = 14.sp,
                color = when (agenda.estado.lowercase()) {
                    "pendiente" -> Color(0xFFFFA000)
                    "completada" -> Color(0xFF4CAF50)
                    else -> Color.Gray
                }
            )
        }
    }
}

@Composable
fun AddCitaDialog(
    selectedDate: LocalDate,
    pacienteId: String,
    onAddCita: (AgendaCreateRequest) -> Unit,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    val context = LocalContext.current
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Nueva Cita") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Título de la cita") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                selectedTime = LocalTime.of(hour, minute)
                            },
                            selectedTime.hour,
                            selectedTime.minute,
                            true
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seleccionar hora: ${selectedTime.format(timeFormatter)}")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Aquí está el código nuevo
                    val fechaFormateada = selectedDate.format(DateTimeFormatter.ISO_DATE)
                    val horaFormateada = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

                    val agendaRequest = AgendaCreateRequest(
                        PacienteID = pacienteId.toInt(),
                        nombre = nombre,
                        descripcion = descripcion,
                        Fecha = "${fechaFormateada}T00:00:00Z",
                        Hora = "${fechaFormateada}T${horaFormateada}Z"
                    )
                    onAddCita(agendaRequest)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
fun formatearFecha(fechaStr: String): String {
    return try {
        val fecha = Instant.parse(fechaStr)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: Exception) {
        try {
            val fecha = LocalDateTime.parse(fechaStr.removeSuffix("Z"))
                .toLocalDate()
            fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            fechaStr
        }
    }
}

fun formatearHora(horaStr: String): String {
    return try {
        val hora = Instant.parse(horaStr)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
        hora.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        try {
            val hora = LocalDateTime.parse(horaStr.removeSuffix("Z"))
                .toLocalTime()
            hora.format(DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: Exception) {
            horaStr
        }
    }
}