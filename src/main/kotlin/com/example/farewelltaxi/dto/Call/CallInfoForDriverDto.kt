package com.example.farewelltaxi.dto.Call

import com.example.farewelltaxi.enum.Status

data class CallInfoForDriverDto(
    val origin: String,
    val destination: String,
    val price: Int,
    val passengerNickname: String?,
    val status: Status?
)