package com.example.wanderlogapp.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wanderlogapp.data.Viagem
import com.example.wanderlogapp.data.ViagemDAO

@Composable
fun EditarViagemScreen(navController: NavController, viagemId: String?) {
    val viagemDAO = remember { ViagemDAO() }
    var local by remember { mutableStateOf("") }
    var comentario by remember { mutableStateOf("") }
    var isEditing = viagemId != "new"

    LaunchedEffect(viagemId) {
        if (isEditing) {
            viagemDAO.buscarViagens(
                onSuccess = { viagens ->
                    viagens.find { it.id == viagemId }?.let {
                        local = it.local
                        comentario = it.comentario
                    }
                },
                onFailure = { /* Tratar erro */ }
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(if (isEditing) "Editar Viagem" else "Nova Viagem", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = local, onValueChange = { local = it }, label = { Text("Local") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = comentario, onValueChange = { comentario = it }, label = { Text("Comentário") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val viagem = Viagem(id = viagemId ?: "", local = local, comentario = comentario)

            if (isEditing) {
                viagemDAO.atualizarViagem(viagem, { navController.popBackStack() }, { /* Tratar erro */ })
            } else {
                viagemDAO.adicionarViagem(viagem, { navController.popBackStack() }, { /* Tratar erro */ })
            }
        }) {
            Text(if (isEditing) "Salvar" else "Adicionar")
        }

        if (isEditing) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viagemDAO.deletarViagem(viagemId!!, { navController.popBackStack() }, { /* Tratar erro */ })
            }) {
                Text("Deletar Viagem")
            }
        }
    }
}
