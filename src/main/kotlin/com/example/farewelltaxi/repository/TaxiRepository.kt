package com.example.farewelltaxi.repository
import com.example.farewelltaxi.model.Taxi
import org.springframework.data.jpa.repository.JpaRepository

interface TaxiRepository : JpaRepository<Taxi, Long> {
}