package com.example.wanderlogapp.data

data class Viagem(
    val id: String = "",
    val local: String = "",
    val comentario: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    val imagens: List<String> = emptyList(),
    val userId: String = ""
)



