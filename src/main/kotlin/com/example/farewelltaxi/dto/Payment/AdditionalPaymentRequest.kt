package com.example.farewelltaxi.dto.Payment

data class AdditionalPaymentRequest(
    val passengerId: Long,
    val callId: Long,
    val driverId: Long,
    val initialPaymentId: String, // 최초 결제 ID
    val additionalPaymentId: String // 추가 결제 ID


)
