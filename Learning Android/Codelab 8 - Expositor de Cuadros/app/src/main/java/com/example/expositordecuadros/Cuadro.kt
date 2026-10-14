package com.example.expositordecuadros

import androidx.compose.ui.graphics.Color

data class Cuadro(
    val titulo: String,      // Título de la pintura (ej: "La Noche Estrellada")
    val autor: String,       // Nombre del artista (ej: "Vincent van Gogh")
    val redSocial: String,   // Red Social / Contacto (24sp)
)