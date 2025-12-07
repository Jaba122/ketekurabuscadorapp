package com.example.ketekura.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("username") val username: String,
    @SerializedName("new_password") val newPassword: String
)
