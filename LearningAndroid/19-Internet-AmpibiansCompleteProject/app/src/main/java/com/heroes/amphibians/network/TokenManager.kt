package com.heroes.amphibians.network

class TokenManager {
    private var token: String? = null

    fun saveToken(newToken: String) {
        token = newToken
    }

    fun getToken(): String? = token

    fun clearToken() {
        token = null
    }

    fun hasToken(): Boolean = !token.isNullOrEmpty()
}