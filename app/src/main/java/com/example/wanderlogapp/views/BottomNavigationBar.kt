package com.example.wanderlogapp.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.wanderlogapp.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar (
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primary // Cor do container
    ) {
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_navigation), contentDescription = "Home") },
            label = { Text("Home") },
            selected = false, // Vai ser dinâmico
            onClick = {
                navController.navigate("homeScreen") // Substitua por sua tela inicial
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_navigation), contentDescription = "Search") },
            label = { Text("Search") },
            selected = false,
            onClick = {
                navController.navigate("searchScreen") // Substitua pela tela desejada
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.ic_navigation), contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = {
                navController.navigate("profileScreen") // Substitua pela tela de perfil
            },
            alwaysShowLabel = true
        )
    }
}
