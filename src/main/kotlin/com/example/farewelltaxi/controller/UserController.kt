package com.example.farewelltaxi.controller

import com.example.farewelltaxi.dto.USER.LoginRequest
import com.example.farewelltaxi.dto.USER.LoginResponse
import com.example.farewelltaxi.dto.USER.SignUpRequest
import com.example.farewelltaxi.dto.USER.UserResponse
import com.example.farewelltaxi.enum.Role
import com.example.farewelltaxi.service.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
    @RequestMapping("/user")
    class UserController(private val userService: UserService) {


        //로그인
        @PostMapping("/login")
        fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
            val loginResponse = userService.login(loginRequest)
            return ResponseEntity.ok(loginResponse)
        }

    }