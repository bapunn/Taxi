package com.example.farewelltaxi.dto.Driver

import com.example.farewelltaxi.enum.Role

data class DriverResponseDto (
    var name:String,//기사 이름
    var email: String,//기사 메일
    var password:String,//기사 비번
    var role: Role,
    var nickname:String

    )

