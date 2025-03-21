package com.example.wanderlogapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wanderlogapp.R
import com.example.wanderlogapp.auth.Nohemi
import com.example.wanderlogapp.data.Viagem
import com.example.wanderlogapp.data.ViagemDAO
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

@Composable
fun MinhasViagensScreen(navController: NavController) {
    val viagemDAO = remember { ViagemDAO() }
    var viagens by remember { mutableStateOf(listOf<Viagem>()) }
    var isLoading by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var viagemToDelete by remember { mutableStateOf<Viagem?>(null) }
    var showUserInfoDialog by remember { mutableStateOf(false) }

    val user = FirebaseAuth.getInstance().currentUser // Obtenha o usuário atual
    val userEmail = user?.email ?: "E-mail não disponível"

    LaunchedEffect(Unit) {
        val userId = user?.uid
        if (userId != null) {
            viagemDAO.buscarViagens(
                userId = userId,  // Passando o userId para filtrar as viagens
                onSuccess = {
                    viagens = it
                    isLoading = false
                },
                onFailure = {
                    isLoading = false
                }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White) // Cor de fundo branca
            .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Removendo o fundo retangular da logo
                Image(
                    painter = painterResource(id = R.drawable.logowanderlog), // Logo menor
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(40.dp) // Diminuindo o tamanho da logo
                        .weight(0.6f) // Faz a logo usar o espaço disponível na linha
                        .padding(end = 16.dp) // Espaçamento entre logo e ícone
                )

                // Ícone de conta no canto
                IconButton(onClick = { showUserInfoDialog = true }) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Informações do Usuário")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título "Minhas Viagens"
            Text(
                text = "Minhas Viagens",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = Nohemi),
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Viagens
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(viagens.chunked(2)) { viagensLinha ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            viagensLinha.forEach { viagem ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f) // Divide igualmente o espaço entre as colunas
                                        .clickable { navController.navigate("editarViagemScreen/${viagem.id}") }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = viagem.local, style = MaterialTheme.typography.titleMedium)
                                        Text(
                                            text = viagem.comentario.take(100) + if (viagem.comentario.length > 100) "..." else "",
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Row {
                                            IconButton(onClick = {
                                                navController.navigate("editarViagemScreen/${viagem.id}")
                                            }) {
                                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Viagem")
                                            }

                                            IconButton(onClick = {
                                                viagemToDelete = viagem
                                                showDeleteDialog = true
                                            }) {
                                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Deletar Viagem")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Botão de adicionar viagem
        FloatingActionButton(
            onClick = {
                navController.navigate("editarViagemScreen/${UUID.randomUUID()}")  // Gerar um novo ID para a criação da viagem
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Viagem")
        }

        // Caixa de diálogo para deletar viagem
        if (showDeleteDialog && viagemToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Deletar Viagem") },
                text = { Text("Você realmente deseja deletar essa viagem?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viagemToDelete?.let {
                                viagemDAO.deletarViagem(it.id, {
                                    viagens = viagens.filterNot { v -> v.id == it.id }
                                    showDeleteDialog = false
                                }, {
                                    // Tratar erro
                                })
                            }
                        }
                    ) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Não")
                    }
                }
            )
        }

        // Caixa de diálogo para informações do usuário
        if (showUserInfoDialog) {
            AlertDialog(
                onDismissRequest = { showUserInfoDialog = false },
                title = { Text("Informações do Usuário") },
                text = {
                    Column {
                        Text("E-mail: $userEmail")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Realiza o logout
                            FirebaseAuth.getInstance().signOut()

                            // Navega para a tela de login e limpa a pilha de navegação
                            navController.navigate("loginScreen") {
                                popUpTo("homeScreen") { inclusive = true } // Remove a tela de home da pilha
                            }

                            // Fecha o dialog
                            showUserInfoDialog = false
                        }
                    ) {
                        Text("Sair")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUserInfoDialog = false }) {
                        Text("Fechar")
                    }
                }
            )
        }
    }
}