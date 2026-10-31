package com.example.dessertclicker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    private val desserts = Datasource.dessertList

    /**
     * Maneja el clic en el postre:
     * - Aumenta las ventas y los ingresos
     * - Determina y actualiza el siguiente postre a mostrar
     */
    fun onDessertClicked() {
        _uiState.update {
            val dessertsSold = it.dessertsSold + 1
            val dessertToShow = determineDessertToShow(dessertsSold)

            it.copy(
                revenue = it.revenue + it.currentDessertPrice,
                dessertsSold = dessertsSold,
                currentDessertImageId = dessertToShow.imageId,
                currentDessertPrice = dessertToShow.price
            )
        }

    }

    /**
     * Determina qué postre mostrar según la cantidad vendida.
     */
    fun determineDessertToShow(dessertsSold: Int): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }


}