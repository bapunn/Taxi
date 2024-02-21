package com.example.farewelltaxi.repository

import com.example.farewelltaxi.model.FavoriteLocation
import org.springframework.data.jpa.repository.JpaRepository


interface FavoriteLocationRepository : JpaRepository<FavoriteLocation, Long> {
}
