package com.example.farewelltaxi.dto.Payment

import kotlinx.serialization.Serializable

@Serializable
data class PaymentDetails(
    val paymentId: String,
    val amount: Int,
)