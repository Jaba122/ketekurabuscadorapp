package com.example.ketekura.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("status") val status: String
)
