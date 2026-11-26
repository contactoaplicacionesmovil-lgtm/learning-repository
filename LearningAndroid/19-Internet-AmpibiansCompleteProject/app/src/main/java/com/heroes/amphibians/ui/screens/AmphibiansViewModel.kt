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
import retrofit2.HttpException

class AmphibiansViewModel(private val repository: AmphibiansRepository) : ViewModel() {

    var amphibiansUiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    var isAuthenticated by mutableStateOf(false)
        private set

    var isLoggingIn by mutableStateOf(false)
        private set

    var loginErrorMessage by mutableStateOf<String?>(null)
        private set


    /*    LUEGO DEL LOGIN
        init {
            getAmphibians()
        }*/

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoggingIn = true
            loginErrorMessage = null

            try {
                repository.login(email, password)
                isAuthenticated = true
                getAmphibians()
            } catch (e: HttpException) {
                loginErrorMessage =
                    if (e.code() == 401) "Correo o contraseña incorrectos."
                    else "Error del servidor: ${e.code()}."
            } catch (e: Exception) {
                loginErrorMessage = "No se pudo conectar con el servicio."
            } finally {
                isLoggingIn = false
            }
        }
    }

    fun getAmphibians() = viewModelScope.launch {
        amphibiansUiState = AmphibiansUiState.Loading

        try {
            amphibiansUiState =
                AmphibiansUiState.Success(repository.getAmphibians())
        } catch (e: HttpException) {
            if (e.code() == 401) {
                repository.logout()
                isAuthenticated = false
                loginErrorMessage = "La sesión venció. Inicia sesión nuevamente."
            }
            amphibiansUiState = AmphibiansUiState.Error
        } catch (e: Exception) {
            amphibiansUiState = AmphibiansUiState.Error
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
