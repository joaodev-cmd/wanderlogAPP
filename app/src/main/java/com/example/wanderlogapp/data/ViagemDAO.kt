package com.example.wanderlogapp.data

import com.google.firebase.firestore.FirebaseFirestore

class ViagemDAO {
    private val db = FirebaseFirestore.getInstance()
    private val viagensCollection = db.collection("viagens")

    fun deletarViagem(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viagensCollection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun buscarViagens(userId: String, onSuccess: (List<Viagem>) -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("viagens")
            .whereEqualTo("userId", userId) // Filtra pelo userId
            .get()
            .addOnSuccessListener { result ->
                val viagens = result.map { document ->
                    document.toObject(Viagem::class.java)
                }
                onSuccess(viagens)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun salvarViagem(viagem: Viagem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val viagemRef = db.collection("viagens").document(viagem.id)

        viagemRef.set(viagem)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun buscarViagemPorId(id: String, onSuccess: (Viagem?) -> Unit, onFailure: (Exception) -> Unit) {
        viagensCollection.document(id).get()
            .addOnSuccessListener { document ->
                val viagem = document.toObject(Viagem::class.java)
                onSuccess(viagem)
            }
            .addOnFailureListener { onFailure(it) }
    }
}
