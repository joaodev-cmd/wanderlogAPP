package com.example.wanderlogapp.views

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider

@Composable
fun MapsScreen(viewModel: MapsViewModel = viewModel()) {
    val cameraPositionState = rememberCameraPositionState()
    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }
    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var description by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5f) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val markers = remember { mutableStateListOf<MarkerData>() }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
            onMapLoaded = { viewModel.fetchLocation() },
            onMapClick = { latLng ->
                selectedLocation = latLng
                showDialog = true
            }
        ) {
            viewModel.location?.let { latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Localização Atual"
                )
            }
            markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = marker.latLng),
                    title = marker.description,
                    snippet = "Nota: ${marker.rating}/5",
                    onClick = {
                        viewModel.selectedMarker = marker
                        viewModel.showMarkerDialog = true
                        true
                    }
                )
            }
        }

        Button(onClick = { viewModel.fetchLocation() }) {
            Text(text = "Buscar Localização")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Adicionar Local") },
            text = {
                Column {
                    Text("Descrição:")
                    BasicTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .border(1.dp, Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Nota: ${rating.toInt()}/5")
                    Slider(value = rating, onValueChange = { rating = it }, valueRange = 1f..5f)

                    Spacer(modifier = Modifier.height(8.dp))

                    val galleryLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        uri?.let { imageUri = it }
                    }

                    Button(onClick = { galleryLauncher.launch("image/*") }) {
                        Text("Escolher Imagem")
                    }

                    imageUri?.let {
                        Image(
                            painter = rememberImagePainter(it),
                            contentDescription = "Foto do local",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    selectedLocation?.let {
                        markers.add(MarkerData(it, description, rating, imageUri))
                        description = ""
                        rating = 5f
                        imageUri = null
                        showDialog = false
                    }
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (viewModel.showMarkerDialog && viewModel.selectedMarker != null) {
        AlertDialog(
            onDismissRequest = { viewModel.showMarkerDialog = false },
            title = { Text(viewModel.selectedMarker!!.description) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Nota: ${viewModel.selectedMarker!!.rating}/5")

                    viewModel.selectedMarker!!.imageUri?.let {
                        Image(
                            painter = rememberImagePainter(it),
                            contentDescription = "Foto do local",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.showMarkerDialog = false }) {
                    Text("Fechar")
                }
            }
        )
    }
}

data class MarkerData(val latLng: LatLng, val description: String, val rating: Float, val imageUri: Uri?)

class MapsViewModel : androidx.lifecycle.ViewModel() {
    private val apiKey = "SUA_CHAVE_API"
    var location by mutableStateOf<LatLng?>(null)
    var showMarkerDialog by mutableStateOf(false)
    var selectedMarker by mutableStateOf<MarkerData?>(null)

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
