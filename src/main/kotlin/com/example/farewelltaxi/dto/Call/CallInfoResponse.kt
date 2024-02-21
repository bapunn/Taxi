package com.example.farewelltaxi.dto.Call

data class CallInfoResponse(
        val origin: String,
        val destination: String,
        val nomaltaxiprice: Int, // 경로 비용, 타입은 필요에 따라 조정
        val bluetaxiprice: Int, // 경로 비용, 타입은 필요에 따라 조정
        val blacktaxiprice: Int, // 경로 비용, 타입은 필요에 따라 조정
        val originCoordinates: String,
        val destinationCoordinates: String
        // 여기에 다른 필요한 필드를 추가할 수 있습니다.
    )


