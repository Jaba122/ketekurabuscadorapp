package com.example.ketekura.repository

import com.example.ketekura.network.LoginRequest
import com.example.ketekura.network.RetrofitInstance
import com.example.ketekura.util.TokenManager

class AuthRepository {

    private val api = RetrofitInstance.api

    suspend fun login(username: String, password: String): String? {
        val response = api.login(LoginRequest(username, password))

        return if (response.status == "ok" && response.token != null) {
            TokenManager.token = response.token
            response.role
        } else {
            null
        }
    }
}