package com.example.farewelltaxi.repository

import com.example.farewelltaxi.model.Admin
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<Admin?, Long?>