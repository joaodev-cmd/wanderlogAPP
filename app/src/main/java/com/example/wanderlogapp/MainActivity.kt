package com.example.wanderlogapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wanderlogapp.ui.theme.WanderlogAPPTheme
import com.example.wanderlogapp.auth.LoginScreen
import com.example.wanderlogapp.auth.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanderlogAPPTheme {
                // Definindo o NavController
                val navController = rememberNavController()

                // Criação do NavHost
                NavHost(navController = navController, startDestination = "signUpScreen") {
                    // Composable para a tela de cadastro
                    composable("signUpScreen") {
                        SignUpScreen(navController)
                    }
                    // Composable para a tela de login
                    composable("loginScreen") {
                        LoginScreen(navController)
                    }
                }
            }
        }
    }
}
