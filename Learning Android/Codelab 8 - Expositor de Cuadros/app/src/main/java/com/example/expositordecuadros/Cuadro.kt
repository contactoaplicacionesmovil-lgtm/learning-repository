package com.example.expositordecuadros

import androidx.compose.ui.graphics.Color

data class Cuadro(
    val titulo: String,      // Título de la pintura (ej: "La Noche Estrellada")
    val autor: String,       // Nombre del artista (ej: "Vincent van Gogh")
    val anio: String,        // Año de creación (ej: "1889")
    val colorFondo: Color    // Color de muestra mientras se reemplaza por imágenes reales
)