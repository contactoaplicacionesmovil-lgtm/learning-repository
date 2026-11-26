package com.heroes.amphibians.data

import com.heroes.amphibians.model.Amphibian
import com.heroes.amphibians.network.AmphibiansApiService
import com.heroes.amphibians.model.LoginRequest
import com.heroes.amphibians.network.TokenManager

/**
 * Interfaz que define las operaciones del repositorio.
 * Esto permite cambiar la implementación fácilmente en el futuro (ej. para pruebas).
 */
interface AmphibiansRepository {

    suspend fun login(email: String, password: String): String
    suspend fun getAmphibians(): List<Amphibian>

    fun logout()
}

class NetworkAmphibiansRepository(
    private val apiService: AmphibiansApiService,
    private val tokenManager: TokenManager
) : AmphibiansRepository {

    override suspend fun login(email: String, password: String): String {
        val response = apiService.login(LoginRequest(email, password))
        tokenManager.saveToken(response.accessToken)
        return response.accessToken
    }

    override suspend fun getAmphibians(): List<Amphibian> {
        return apiService.getAmphibians()
    }

    override fun logout() {
        tokenManager.clearToken()
    }
}