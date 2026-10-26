package com.heroes.myapplication.ui.home


import com.heroes.myapplication.data.model.ItemDinamico

data class HomeUiState(
    val isLoading: Boolean = false,
    val items: List<ItemDinamico> = emptyList(),
    val errorMessage: String? = null
)