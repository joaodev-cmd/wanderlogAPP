package com.example.wanderlogapp.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier
import com.example.wanderlogapp.views.EditarViagemScreen
import com.example.wanderlogapp.views.MapsScreen
import com.example.wanderlogapp.views.MinhasViagensScreen

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) } // Adiciona o menu inferior
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavigationGraph(navController) // Aqui ficam as telas do app
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "homeScreen"
    ) {
        composable("homeScreen") { MinhasViagensScreen(navController) }
        composable("mapsScreen") { MapsScreen() }
        composable("profileScreen") { MinhasViagensScreen(navController) }
    }
}
