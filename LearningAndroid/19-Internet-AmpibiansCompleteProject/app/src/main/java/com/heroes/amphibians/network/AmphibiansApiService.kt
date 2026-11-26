package com.heroes.amphibians.network

import com.heroes.amphibians.model.Amphibian
import com.heroes.amphibians.model.LoginRequest
import com.heroes.amphibians.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AmphibiansApiService {

    @GET("api/frogs")
    suspend fun getAmphibians(
    ): List<Amphibian>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

}