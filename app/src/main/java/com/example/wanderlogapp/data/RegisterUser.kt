package com.example.wanderlogapp.data

import com.google.firebase.auth.FirebaseAuth

fun registerUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    if (password.length < 6) {
        onFailure("A senha deve ter pelo menos 6 caracteres.")
        return
    }

    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception?.message ?: "Erro desconhecido")
            }
        }
}

