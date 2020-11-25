package com.example.teamprojmobv.Request

data class LoggedUser(
    val id: Int,
    val username: String,
    val email: String,
    val token: String,
    val refresh: String,
    val profile: String
)