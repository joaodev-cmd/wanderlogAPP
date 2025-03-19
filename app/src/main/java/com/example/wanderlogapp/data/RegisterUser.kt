package com.example.wanderlogapp.data

import com.google.firebase.auth.FirebaseAuth

fun registerUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                task.exception?.let { exception ->
                    onFailure(exception.message ?: "Unknown error")
                }
            }
        }
}
