package com.example.farewelltaxi.dto.Passenger

import com.example.farewelltaxi.enum.Role

class PassengerResponseDto (
    var name:String,
    var email:String,
    var password:String,
    var nickname:String,
    var role: Role

)