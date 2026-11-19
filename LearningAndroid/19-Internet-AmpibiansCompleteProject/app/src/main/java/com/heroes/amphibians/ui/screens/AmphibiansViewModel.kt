package com.heroes.amphibians.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.heroes.amphibians.AmphibiansApplication
import com.heroes.amphibians.data.AmphibiansRepository
import com.heroes.amphibians.model.Amphibian
import kotlinx.coroutines.launch
import okio.IOException

class AmphibiansViewModel(private val repository: AmphibiansRepository) : ViewModel() {

    var amphibiansUiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    var isAuthenticated by mutableStateOf(false)
        private set

    var isLoggingIn by mutableStateOf(false)
        private set

    var loginErrorMessage by mutableStateOf<String?>(null)
        private set

    private var accessToken: String? = null

/*    LUEGO DEL LOGIN
    init {
        getAmphibians()
    }*/

    /**
     * Obtiene la información de los anfibios desde la fuente de datos.
     */
    fun getAmphibians() {
        viewModelScope.launch {
            amphibiansUiState = AmphibiansUiState.Loading
            amphibiansUiState = try {
                val listResult = repository.getAmphibians()
                AmphibiansUiState.Success(listResult)
            } catch (e: IOException) {
                Log.d("AmphibiansViewModel", "Error: ${e.message}")
                AmphibiansUiState.Error
            } catch (e: Exception) {
                Log.d("AmphibiansViewModel", "Error: ${e.message}")
                AmphibiansUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AmphibiansApplication)
                val amphibiansRepository = application.container.amphibiansRepository
                AmphibiansViewModel(repository = amphibiansRepository)
            }
        }
    }

}
