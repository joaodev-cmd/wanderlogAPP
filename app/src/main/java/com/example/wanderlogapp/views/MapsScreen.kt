package com.example.wanderlogapp.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@SuppressLint("MissingPermission")
@Composable
fun MapsScreen(navController: NavController) {
    val firestore = FirebaseFirestore.getInstance()
    var markers by remember { mutableStateOf(emptyList<LatLng>()) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

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

    // A função composable para exibir o mapa
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true),
        onMapClick = { latLng ->
            selectedLocation = latLng
        }
    ) {
        markers.forEach { location ->
            Marker(state = MarkerState(position = location), title = "Local Salvo")
        }

        // Adiciona o marcador dinamicamente quando o mapa é clicado
        selectedLocation?.let {
            Marker(state = MarkerState(position = it), title = "Local Selecionado")
        }
    }

    // Se um local foi selecionado, navegue de volta com as coordenadas
    selectedLocation?.let {
        navController.previousBackStackEntry?.savedStateHandle?.set("selectedLocation", it)
        navController.popBackStack() // Volta para a tela anterior
    }
}

