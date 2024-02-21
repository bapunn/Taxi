package com.example.farewelltaxi.dto.FavoriteLocation

import com.example.farewelltaxi.model.Passenger

data class FavoriteLocationResponseDto(
    val locationName :String,
    val coordinates :String,
    val passenger :Passenger
)