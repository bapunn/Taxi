package com.example.farewelltaxi.dto.USER

import com.example.farewelltaxi.enum.Role
import javax.crypto.SecretKey

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val nickname:String
    // 필요한 경우 추가적인 필드를 여기에 포함시킬 수 있습니다.
)

