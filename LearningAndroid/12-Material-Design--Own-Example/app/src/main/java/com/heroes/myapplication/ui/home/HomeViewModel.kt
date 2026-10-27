package com.heroes.myapplication.ui.home

import androidx.lifecycle.ViewModel
import com.heroes.myapplication.R
import com.heroes.myapplication.data.model.ItemDinamico
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {

        // Creamos los datos falsos
        val listaItems = listOf(
            ItemDinamico(1, "Bulbasaur", "Tiene una semilla en el lomo desde que nace. Absorbe los rayos del sol para crecer y hacerse más fuerte. Es muy leal a su entrenador.Tiene una semilla en el lomo desde que nace. Absorbe los rayos del sol para crecer y hacerse más fuerte. Es muy leal a su entrenador.Tiene una semilla en el lomo desde que nace. Absorbe los rayos del sol para crecer y hacerse más fuerte. Es muy leal a su entrenador.Tiene una semilla en el lomo desde que nace. Absorbe los rayos del sol para crecer y hacerse más fuerte. Es muy leal a su entrenador.", R.drawable._002, 1),
            ItemDinamico(
                2,
                "Charmander",
                "La llama de su cola indica su salud.",
                R.drawable._006,
                1
            ),
            ItemDinamico(3, "Caterpie", "Segrega un olor fuerte.", R.drawable._010, 1),
            ItemDinamico(4, "Butterfree", "Vuela bajo la lluvia.", R.drawable._012, 1),

            // Tipo 2: Tarjeta ancha con imagen a la derecha
            ItemDinamico(5, "Card Imagen Derecha", "Ocupa todo el ancho.", R.drawable._038, 2),

            // Más fichas chiquitas
            ItemDinamico(6, "Kakuna", "Se endurece para proteger su interior.", R.drawable._014, 1),
            ItemDinamico(7, "Beedrill", "Ataca usando sus aguijones.", R.drawable._016, 1),

            // Tipo 4: Tarjeta ancha con imagen a la izquierda
            ItemDinamico(8, "Card Imagen Izquierda", "Ocupa todo el ancho.", R.drawable._039, 4),

            // Tipo 3: Tarjeta Premium
            ItemDinamico(9, "Card Grande", "Imagen de fondo.", R.drawable._006, 3)
        )

        _uiState.value = HomeUiState(
            isLoading = false,
            items = listaItems
        )
    }

}