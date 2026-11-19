package com.heroes.amphibians.data

import com.heroes.amphibians.network.AmphibiansApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * Contenedor de dependencias para la aplicación.
 */
interface AppContainer {
    val amphibiansRepository: AmphibiansRepository
}

/**
 * Implementación del contenedor que provee las dependencias de red.
 */
class DefaultAppContainer : AppContainer {

    //private val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com/"
    private val baseUrl = "http://10.0.2.2:5256/"

    // Configuración de Retrofit con el conversor de JSON de Kotlinx Serialization
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    // Creación perezosa (lazy) del servicio de Retrofit.
    // Solo se inicializará la primera vez que se necesite.
    private val retrofitService: AmphibiansApiService by lazy {
        retrofit.create(AmphibiansApiService::class.java)
    }

    // Inicialización de nuestro repositorio.
    override val amphibiansRepository: AmphibiansRepository by lazy {
        NetworkAmphibiansRepository(retrofitService)
    }
}