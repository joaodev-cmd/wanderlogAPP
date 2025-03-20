package com.example.wanderlogapp.views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.wanderlogapp.data.Viagem
import com.example.wanderlogapp.data.ViagemDAO
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

@Composable
fun EditarViagemScreen(navController: NavController, viagemId: String?) {
    val viagemDAO = remember { ViagemDAO() }
    var local by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var imagens by remember { mutableStateOf<List<String>>(emptyList()) }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }

    // Gere o idViagem logo ao entrar na tela
    val idViagem = remember { viagemId ?: UUID.randomUUID().toString() }

    // Buscar os dados da viagem se estiver editando
    LaunchedEffect(viagemId) {
        if (viagemId != null) {
            viagemDAO.buscarViagemPorId(
                viagemId,
                onSuccess = { viagemEncontrada ->
                    if (viagemEncontrada != null) {
                        local = viagemEncontrada.local
                        comentario = viagemEncontrada.comentario
                        imagens = viagemEncontrada.imagens
                        latitude = viagemEncontrada.latitude
                        longitude = viagemEncontrada.longitude
                    }
                },
                onFailure = {

                }
            )
        }

        // Observar o estado salvo para a localização
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<LatLng>("selectedLocation")?.observeForever { selectedLatLng ->
            if (selectedLatLng != null) {
                // Atualiza as variáveis de latitude e longitude
                latitude = selectedLatLng.latitude
                longitude = selectedLatLng.longitude
            }
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { imagens = imagens + uri.toString() } }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(androidx.compose.foundation.rememberScrollState())
        ) {
            OutlinedTextField(
                value = local,
                onValueChange = { local = it },
                label = { Text("Nome da Viagem") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Notas") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão para navegar até a tela de mapas
            Button(
                onClick = { navController.navigate("mapsScreen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Escolher Local no Mapa")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow {
                items(imagens) { imagemUri ->
                    Image(
                        painter = rememberAsyncImagePainter(imagemUri),
                        contentDescription = "Imagem da viagem",
                        modifier = Modifier
                            .padding(4.dp)
                            .width(150.dp)
                            .height(150.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = { imagePicker.launch("image/*") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Imagem")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (local.isNotBlank() && comentario.isNotBlank()) {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            val viagem = Viagem(idViagem, local, comentario, longitude, latitude, imagens, user.uid)
                            viagemDAO.salvarViagem(
                                viagem = viagem,
                                onSuccess = { navController.popBackStack() },
                                onFailure = { /* Tratar erro ao salvar */ }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        }
    }
}
