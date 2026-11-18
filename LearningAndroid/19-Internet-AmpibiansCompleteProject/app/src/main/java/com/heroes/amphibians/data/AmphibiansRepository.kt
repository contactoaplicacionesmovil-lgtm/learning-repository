package com.heroes.amphibians.data

import com.heroes.amphibians.model.Amphibian
import com.heroes.amphibians.network.AmphibiansApiService

/**
 * Interfaz que define las operaciones del repositorio.
 * Esto permite cambiar la implementación fácilmente en el futuro (ej. para pruebas).
 */
interface AmphibiansRepository {
    suspend fun getAmphibians(): List<Amphibian>
}

class NetworkAmphibiansRepository(
    private val apiService: AmphibiansApiService
) : AmphibiansRepository {
    override suspend fun getAmphibians(): List<Amphibian> {
        return apiService.getAmphibians()
    }
}