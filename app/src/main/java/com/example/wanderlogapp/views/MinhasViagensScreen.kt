package com.example.wanderlogapp.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wanderlogapp.data.Viagem
import com.example.wanderlogapp.data.ViagemDAO

@Composable
fun MinhasViagensScreen(navController: NavController) {
    val viagemDAO = remember { ViagemDAO() }
    var viagens by remember { mutableStateOf(listOf<Viagem>()) }

    LaunchedEffect(Unit) {
        viagemDAO.buscarViagens(
            onSuccess = { viagens = it },
            onFailure = { /* Tratar erro */ }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Minhas Viagens", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(viagens) { viagem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate("editarViagemScreen/${viagem.id}") }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = viagem.local, style = MaterialTheme.typography.titleMedium)
                        Text(text = viagem.comentario, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("editarViagemScreen/new") }) {
            Text("Adicionar Viagem")
        }
    }
}
