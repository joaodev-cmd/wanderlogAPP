package com.example.wanderlogapp.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wanderlogapp.R
import com.google.firebase.auth.FirebaseAuth

val Nohemi = FontFamily(
    Font(R.font.nohemilight, FontWeight.Light),
    Font(R.font.nohemiregular, FontWeight.Normal),
    Font(R.font.nohemimedium, FontWeight.Medium),
    Font(R.font.nohemisemibold, FontWeight.SemiBold),
    Font(R.font.nohemibold, FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.3f))

        Image(
            painter = painterResource(id = R.drawable.logowanderlog),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.weight(0.1f))

        Text(
            text = "Faça login na sua conta",
            fontFamily = Nohemi,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            color = Color(0x99000000),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(0.2f))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = "Email",
                    fontFamily = Nohemi,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = Color(0x80000000) // Cor com 50% de opacidade
                )
            },
            modifier = Modifier
                .fillMaxWidth() // Largura máxima igual ao botão
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)), // Borda fina cinza com borda arredondada de 10px
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(10.dp), // Bordas arredondadas
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Fundo transparente
                focusedIndicatorColor = Color.Transparent, // Remove a linha de foco padrão
                unfocusedIndicatorColor = Color.Transparent // Remove a linha não focada
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Campo de texto para a senha
        TextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = "Senha",
                    fontFamily = Nohemi,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = Color(0x80000000) // Cor com 50% de opacidade
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth() // Largura máxima igual ao botão
                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)), // Borda fina cinza com borda arredondada de 10px
            shape = RoundedCornerShape(10.dp), // Bordas arredondadas
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent, // Fundo transparente
                focusedIndicatorColor = Color.Transparent, // Remove a linha de foco padrão
                unfocusedIndicatorColor = Color.Transparent // Remove a linha não focada
            )
        )

        Spacer(modifier = Modifier.weight(0.3f))

        // Botão de login
        Button(
            onClick = {
                isLoading = true
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("homeScreen")
                        } else {
                            errorMessage = "Login falhou: ${task.exception?.message ?: "Erro desconhecido"}"
                        }
                        isLoading = false
                    }
            },
            modifier = Modifier
                .fillMaxWidth() // Largura máxima do botão
                .height(50.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF2E8B92), Color(0xFF22CFDC)) // Degradê aplicado
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Fundo transparente para manter degradê
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(
                        text = "ENTRAR",
                        fontFamily = Nohemi,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = { navController.navigate("signUpScreen") },
            modifier = Modifier.padding(bottom = 24.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF2E8B92)) // Cor do degradê
        ) {
            Text(
                text = "Ainda não tem uma conta? Cadastre-se",
                fontFamily = Nohemi,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp, // Tamanho pequeno
                color = Color(0xFF2E8B92), // Cor com 60% de opacidade para o texto antes de "Cadastre-se"
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
