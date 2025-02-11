package com.example.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarMedicamentoDialog(
    onDismiss: () -> Unit,
    onSave: (Medicamento) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var frecuencia by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Medicamento", fontSize = 18.sp, color = Color(0xFF0099A8)) },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del medicamento") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0099A8),
                        unfocusedBorderColor = Color(0xFF537275)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dosis,
                    onValueChange = { dosis = it },
                    label = { Text("Dosis") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0099A8),
                        unfocusedBorderColor = Color(0xFF537275)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = frecuencia,
                    onValueChange = { frecuencia = it },
                    label = { Text("Frecuencia") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0099A8),
                        unfocusedBorderColor = Color(0xFF537275)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotEmpty() && dosis.isNotEmpty() && frecuencia.isNotEmpty()) {
                        onSave(Medicamento(nombre, dosis, frecuencia))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0099A8))
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF537275))
            ) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}
