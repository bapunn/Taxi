package com.example.farewelltaxi.repository

import com.example.farewelltaxi.model.Driver
import com.example.farewelltaxi.model.Taxi
import org.springframework.data.jpa.repository.JpaRepository

interface DriverRepository : JpaRepository<Driver,Long> {
    fun findByUserEmail(email: String): Driver?
    fun findByTaxiAndIsDrivingTrue(taxi: Taxi): List<Driver>
}
