package com.example.farewelltaxi.dto.Call

import com.example.farewelltaxi.enum.PaymentType
import com.example.farewelltaxi.enum.Status
import com.example.farewelltaxi.enum.TaxiLevel

data class CallCreateResponseDto (
    var origin: String,//출발지(api전 테스트용임시)
    var destination:String,//도착지(api전 테스트용임시)
    var price:Int,//운행가격(api전 테스트용임시)
    var taxiLevel: TaxiLevel,
    var Status: Status?,
    var PassengerId: Long?,
    var PassengerNickname : String?,
    var paymentType: PaymentType
)