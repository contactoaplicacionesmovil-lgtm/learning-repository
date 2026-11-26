package com.heroes.amphibians.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor de OkHttp que adjunta automáticamente el token Bearer en las peticiones.
 */
class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = tokenManager.getToken()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}