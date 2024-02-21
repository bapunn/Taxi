package com.example.farewelltaxi.dto.Payment

data class Payment (
    val passengerId: Long,
    val callId: Long,
    val paymentDetails: PaymentDetails,
    val driverId:Long)

    //결제 비교