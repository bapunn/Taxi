package com.example.farewelltaxi.dto.Payment

data class ChargeRequest(
    val passengerId: Long,
    val paymentId: String // 충전을 위한 결제 ID
)
