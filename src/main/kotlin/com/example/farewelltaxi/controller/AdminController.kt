package com.example.farewelltaxi.controller

import com.example.farewelltaxi.dto.Admin.AdminRequestDto
import com.example.farewelltaxi.dto.Admin.AdminResponseDto
import com.example.farewelltaxi.model.Admin
import com.example.farewelltaxi.service.AdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
class AdminController {
    @Autowired
    private val adminService: AdminService? = null

    @PostMapping("/create")
    fun createAdmin(@RequestBody adminRequestDto: AdminRequestDto, @RequestParam secretKey: String): ResponseEntity<AdminResponseDto> {
        val adminResponseDto = adminService?.createAdmin(adminRequestDto, secretKey)
        return ResponseEntity.ok(adminResponseDto)
    }

}