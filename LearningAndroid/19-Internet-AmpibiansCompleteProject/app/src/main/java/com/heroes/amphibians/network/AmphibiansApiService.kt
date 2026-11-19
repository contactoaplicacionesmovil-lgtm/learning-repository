package com.heroes.amphibians.network

import com.heroes.amphibians.model.Amphibian
import retrofit2.http.GET

interface AmphibiansApiService {

    @GET("api/frogs")
    suspend fun getAmphibians(): List<Amphibian>

}