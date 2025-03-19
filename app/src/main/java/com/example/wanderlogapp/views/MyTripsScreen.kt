package com.example.wanderlogapp.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MyTripsScreen(navController: NavController) {
    // Lista de viagens (você pode substituir isso por uma lista dinâmica com banco de dados)
    val trips = listOf("Viagem 1", "Viagem 2", "Viagem 3")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Minhas Viagens", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // Exibe cada viagem
        trips.forEach { trip ->
            TextButton(onClick = {
                // Navega para a tela de detalhes do diário de viagem
                navController.navigate("tripDetailScreen/$trip")
            }) {
                Text(trip)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para adicionar uma nova viagem
        Button(
            onClick = {
                // Navega para a tela de criação de um novo diário de viagem
                navController.navigate("createTripScreen")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar Nova Viagem")
        }
    }
}
