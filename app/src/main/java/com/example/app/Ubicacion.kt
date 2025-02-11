package com.example.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbicacionScreen(navController: NavController) {
    val context = LocalContext.current
    var permisosConcedidos by remember { mutableStateOf(false) }
    var errorMapa by remember { mutableStateOf<String?>(null) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permisosConcedidos = isGranted
    }

    // Verificar permisos de ubicación
    LaunchedEffect(Unit) {
        permisosConcedidos = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!permisosConcedidos) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ubicación", fontSize = 18.sp, color = Color.White) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF0099A8)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // 🔹 Botón de retroceso
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Retroceder",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (permisosConcedidos) {
                MapboxMapView(context) // 🔹 Llamamos al mapa sin usar `try`
            } else {
                Text(
                    text = "Se necesitan permisos para acceder a la ubicación",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            errorMapa?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun MapboxMapView(context: Context) {
    val mapView = remember {
        MapView(
            context,
            MapInitOptions(
                context,
                resourceOptions = ResourceOptions.Builder()
                    .accessToken("sk.eyJ1IjoidmFsZXNjaHYyIiwiYSI6ImNtNm10NWU0azBuZXkyanBqNHQ0eHdkZnMifQ.SqClKk_ePQN8kJa1pvMcsA") // 🔹 API Key aquí
                    .build()
            )
        )
    }

    AndroidView(
        factory = { mapView },
        update = { view ->
            val mapboxMap = view.getMapboxMap()

            // Cargar el estilo del mapa
            mapboxMap.loadStyleUri(Style.MAPBOX_STREETS) {
                // 🔹 Ubicación falsa en Quevedo, Ecuador
                val fakeLocation = com.mapbox.geojson.Point.fromLngLat(-79.4632, -1.0286)

                // Agregar un marcador en la ubicación falsa
                val annotationApi = view.annotations
                val pointAnnotationManager = annotationApi.createPointAnnotationManager()

                val pointAnnotationOptions = com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions()
                    .withPoint(fakeLocation)
                    .withIconImage("mapbox-marker-icon")

                pointAnnotationManager.create(pointAnnotationOptions)

                // Mover la cámara a la ubicación falsa
                mapboxMap.setCamera(
                    com.mapbox.maps.CameraOptions.Builder()
                        .center(fakeLocation)
                        .zoom(14.0) // 🔹 Nivel de zoom
                        .build()
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
