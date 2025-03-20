package com.example.wanderlogapp.views

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

@Composable
fun MapsScreen(viewModel: MapsViewModel = viewModel()) {
    val cameraPositionState = rememberCameraPositionState()
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapLoaded = {
                viewModel.fetchLocation()
            }
        ) {
            viewModel.location?.let { latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Localização",
                    snippet = "Marcador no local"
                )
            }
        }

        Button(onClick = { viewModel.fetchLocation() }) {
            Text(text = "Buscar Localização")
        }
    }
}

class MapsViewModel : androidx.lifecycle.ViewModel() {
    private val apiKey = "SUA_CHAVE_API"
    var location by mutableStateOf<LatLng?>(null)

    suspend fun getLatLngFromLocationName(locationName: String): LatLng? {
        val client = OkHttpClient()
        val url = "https://maps.googleapis.com/maps/api/geocode/json?address=$locationName&key=$apiKey"

        val request = Request.Builder().url(url).build()

        return try {
            val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
            if (response.isSuccessful) {
                val jsonResponse = response.body?.string()
                val jsonObject = JSONObject(jsonResponse)
                val results = jsonObject.getJSONArray("results")
                if (results.length() > 0) {
                    val geometry = results.getJSONObject(0).getJSONObject("geometry")
                    val location = geometry.getJSONObject("location")
                    val lat = location.getDouble("lat")
                    val lng = location.getDouble("lng")
                    LatLng(lat, lng)
                } else null
            } else null
        } catch (e: IOException) {
            Log.e("MapsViewModel", "Erro ao buscar geocodificação: ${e.message}")
            null
        }
    }

    fun fetchLocation() {
        location = LatLng(-15.7801, -47.9292) // Ponto inicial (Brasília)
    }
}
