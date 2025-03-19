package com.example.wanderlogapp.data

data class Viagem(
    val id: String = "",
    val local: String = "",
    val comentario: String = "",
    val imagens: List<String> = emptyList()
)
