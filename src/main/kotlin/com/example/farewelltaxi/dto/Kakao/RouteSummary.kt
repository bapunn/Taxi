package com.example.farewelltaxi.dto.Kakao

import kotlinx.serialization.Serializable

@Serializable
data class RouteSummary(
    val fare: FareDetails
)
