package com.example.farewelltaxi.service

import com.example.farewelltaxi.dto.USER.LoginRequest
import com.example.farewelltaxi.dto.USER.LoginResponse
import com.example.farewelltaxi.dto.USER.SignUpRequest
import com.example.farewelltaxi.dto.USER.UserResponse
import com.example.farewelltaxi.enum.Role
import org.springframework.stereotype.Service

@Service
interface UserService {

    fun login(loginRequest: LoginRequest):LoginResponse
}