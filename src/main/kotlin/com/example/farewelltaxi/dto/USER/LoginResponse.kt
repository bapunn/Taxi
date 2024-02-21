package com.example.farewelltaxi.dto.USER

data class LoginResponse(
    val email: String,
    val token: String // JWT 토큰
)

