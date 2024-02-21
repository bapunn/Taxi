package com.example.farewelltaxi.dto.Payment

data  class AdditionalPayment(
    val passengerId: Long,
    val callId: Long,
    val driverId: Long,
    val initialPaymentDetails: PaymentDetails,
    val additionalPaymentDetails: PaymentDetails
)
