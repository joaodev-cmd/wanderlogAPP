package com.example.wanderlogapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wanderlogapp.auth.LoginScreen
import com.example.wanderlogapp.auth.SignUpScreen
import com.example.wanderlogapp.ui.theme.WanderlogAPPTheme
import com.example.wanderlogapp.views.EditarViagemScreen
import com.example.wanderlogapp.views.MapsScreen
import com.example.wanderlogapp.views.MinhasViagensScreen

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permissão de localização concedida!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissão de localização negada.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLocationPermission() // Solicita a permissão antes de carregar o conteúdo

        setContent {
            WanderlogAPPTheme {
                AppNavigation()
            }
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "Permissão já concedida!", Toast.LENGTH_SHORT).show()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(this, "O app precisa da localização para funcionar corretamente.", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "loginScreen") {
        composable("loginScreen") { LoginScreen(navController) }
        composable("signUpScreen") { SignUpScreen(navController) }
        composable("homeScreen") { MinhasViagensScreen(navController) }
        composable("editarViagemScreen/{viagemId}") { backStackEntry ->
            val viagemId = backStackEntry.arguments?.getString("viagemId")
            EditarViagemScreen(navController, viagemId)
        }
        composable("mapsScreen") { MapsScreen() }
    }
}
