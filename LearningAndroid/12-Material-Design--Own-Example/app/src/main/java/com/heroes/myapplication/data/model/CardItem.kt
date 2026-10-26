package com.heroes.myapplication.data.model

import androidx.annotation.DrawableRes

data class ItemDinamico(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val imagenId: Int,
    // Tipo de tarjeta: 1 = Izquierda, 2 = Derecha, 3 = Premium (Fondo)
    val tipoTarjeta: Int
)