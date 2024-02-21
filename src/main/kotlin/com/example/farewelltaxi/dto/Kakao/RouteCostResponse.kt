package com.example.farewelltaxi.dto.Kakao

import kotlinx.serialization.Serializable

@Serializable
data class RouteCostResponse(
    val routes: List<Route>
)
