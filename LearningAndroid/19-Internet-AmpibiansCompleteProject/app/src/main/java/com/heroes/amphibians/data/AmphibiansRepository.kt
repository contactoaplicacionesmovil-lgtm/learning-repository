package com.heroes.amphibians.data

import com.heroes.amphibians.model.Amphibian
import com.heroes.amphibians.network.AmphibiansApiService
import com.heroes.amphibians.model.LoginRequest

/**
 * Interfaz que define las operaciones del repositorio.
 * Esto permite cambiar la implementación fácilmente en el futuro (ej. para pruebas).
 */
interface AmphibiansRepository {

    suspend fun login(email: String, password: String): String
    suspend fun getAmphibians(accessToken: String): List<Amphibian>
}

class NetworkAmphibiansRepository(
    private val apiService: AmphibiansApiService
) : AmphibiansRepository {

    override suspend fun login(email: String, password: String): String {
        return apiService.login(LoginRequest(email, password)).accessToken
    }

    override suspend fun getAmphibians(accessToken: String): List<Amphibian> {
        return apiService.getAmphibians("Bearer $accessToken")
    }
}