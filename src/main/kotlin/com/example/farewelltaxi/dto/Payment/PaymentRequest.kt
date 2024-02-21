package com.example.farewelltaxi.dto.Payment

data class PaymentRequest(
    val passengerId: Long,
    val callId: Long,
    val paymentId: String, // 결제 ID
    val driverId: Long,
    val amount: Int // 결제 금액 추가
)
