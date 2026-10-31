package com.example.dessertclicker

import com.example.dessertclicker.data.Datasource

const val INITIAL_INDEX = 0

data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = INITIAL_INDEX,
    val currentDessertPrice: Int = Datasource.dessertList[INITIAL_INDEX].price,
    val currentDessertImageId: Int = Datasource.dessertList[INITIAL_INDEX].imageId
)