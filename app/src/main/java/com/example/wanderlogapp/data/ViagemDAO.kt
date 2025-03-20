package com.example.wanderlogapp.data

import com.google.firebase.firestore.FirebaseFirestore

class ViagemDAO {
    private val db = FirebaseFirestore.getInstance()
    private val viagensCollection = db.collection("viagens")

    fun adicionarViagem(viagem: Viagem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val novoDoc = viagensCollection.document()
        val viagemComId = viagem.copy(id = novoDoc.id)

        novoDoc.set(viagemComId)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun atualizarViagem(viagem: Viagem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viagensCollection.document(viagem.id).set(viagem)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deletarViagem(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viagensCollection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun buscarViagens(onSuccess: (List<Viagem>) -> Unit, onFailure: (Exception) -> Unit) {
        viagensCollection.get()
            .addOnSuccessListener { result ->
                val viagens = result.map { it.toObject(Viagem::class.java) }
                onSuccess(viagens)
            }
            .addOnFailureListener { onFailure(it) }
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
}
