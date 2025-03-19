package com.example.wanderlogapp.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
        // Logo

        Spacer(modifier = Modifier.height(16.dp))

        Text("Minhas Viagens", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de viagens
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

                        // Botões para editar e deletar
                        Row {
                            // Botão editar
                            IconButton(onClick = {
                                navController.navigate("editarViagemScreen/${viagem.id}")
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Viagem")
                            }

                            // Botão deletar
                            IconButton(onClick = {
                                viagemDAO.deletarViagem(viagem.id, {
                                    viagens = viagens.filterNot { it.id == viagem.id }
                                }, { /* Tratar erro */ })
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Deletar Viagem")
                            }
                        }
                    }
                }
            }
        }

        // Botão para adicionar nova viagem
        Spacer(modifier = Modifier.height(16.dp))

        FloatingActionButton(
            onClick = { navController.navigate("editarViagemScreen/new") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Viagem")
        }
    }
}

