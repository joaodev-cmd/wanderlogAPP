package com.example.wanderlogapp.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.google.firebase.firestore.FirebaseFirestore

class MapsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapsScreen()
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun MapsScreen() {
    val firestore = FirebaseFirestore.getInstance()
    var markers by remember { mutableStateOf(emptyList<LatLng>()) }

    // Buscar locais do Firebase Firestore
    LaunchedEffect(Unit) {
        firestore.collection("pins")
            .get()
            .addOnSuccessListener { documents ->
                markers = documents.mapNotNull { doc ->
                    val lat = doc.getDouble("latitude")
                    val lng = doc.getDouble("longitude")
                    if (lat != null && lng != null) LatLng(lat, lng) else null
                }
            }
    }

    // Configuração do Mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-15.8267, -47.9218), 12f) // Exemplo: Brasília
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        markers.forEach { location ->
            Marker(state = MarkerState(position = location), title = "Local Salvo")
        }
    }
}
