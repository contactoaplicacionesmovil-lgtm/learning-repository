package com.heroes.amphibians.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heroes.amphibians.model.Amphibian
import kotlinx.coroutines.launch
import okio.IOException

class AmphibiansViewModel : ViewModel() {

    var amphibiansUiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    init {
        getAmphibians()
    }

    /**
     * Obtiene la información de los anfibios desde la fuente de datos.
     */
    fun getAmphibians() {
        viewModelScope.launch {
            amphibiansUiState = AmphibiansUiState.Loading
            amphibiansUiState = try {
                // TODO: Aquí llamaremos al Repository o ApiService cuando implementemos la capa de red
                // Por ahora simulamos una lista o llamada exitosa:
                val listResult = fakeAmphibians // repository.getAmphibians()
                AmphibiansUiState.Success(listResult)
            } catch (e: IOException) {
                AmphibiansUiState.Error
            } catch (e: Exception) {
                AmphibiansUiState.Error
            }
        }
    }

}

private val fakeAmphibians = listOf(
    Amphibian(
        name = "Great Basin Spadefoot",
        type = "Toad",
        description = "This toad spends most of its life underground due to the arid desert conditions in which it lives. Spadefoot toads earn the name because of their hind legs which are wedged to aid in digging. They are typically grey, green, or brown with dark spots.",
        imgSrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png"
    ),
    Amphibian(
        name = "Roraima Bush Toad",
        type = "Toad",
        description = "This toad is typically found in South America.",
        imgSrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/roraima-bush-toad.png"
    )
)