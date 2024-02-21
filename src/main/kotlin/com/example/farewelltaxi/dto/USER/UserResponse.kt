package com.example.farewelltaxi.dto.USER

import com.example.farewelltaxi.enum.Role


data class UserResponse(
    val email: String,
    val name: String,
    val id:Long,
    val role: Role
)
