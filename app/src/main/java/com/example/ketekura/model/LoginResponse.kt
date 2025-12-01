package com.example.ketekura.model

data class LoginResponse(
    val status: String,
    val token: String?,
    val role: String?
)
