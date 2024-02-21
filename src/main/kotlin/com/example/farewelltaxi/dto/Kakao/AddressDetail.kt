package com.example.farewelltaxi.dto.Kakao

import kotlinx.serialization.Serializable

@Serializable
data class AddressDetail(
    val address_name: String, // 주소 이름
    val x: String, // 경도(longitude)
    val y: String  // 위도(latitude)
)