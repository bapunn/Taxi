package com.example.farewelltaxi.repository
import com.example.farewelltaxi.model.Passenger
import org.springframework.data.jpa.repository.JpaRepository

interface PassengerRepository :JpaRepository<Passenger, Long> {
    fun findByUserEmail(email: String): Passenger?

}
