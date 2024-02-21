package com.example.farewelltaxi.dto.Kakao
import kotlinx.serialization.Serializable

@Serializable
data class AddressSearchResponse(
    val documents: List<AddressDetail>
)



