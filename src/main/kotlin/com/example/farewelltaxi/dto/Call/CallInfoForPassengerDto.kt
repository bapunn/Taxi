package com.example.farewelltaxi.dto.Call

import com.example.farewelltaxi.enum.TaxiLevel
import java.time.LocalDateTime

data class CallInfoForPassengerDto(
    val endAt: LocalDateTime?,
    val duration: String?,
    val taxiLevel: TaxiLevel,
    val origin: String,
    val destination: String,
    val price: Int
)