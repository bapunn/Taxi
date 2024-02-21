package com.example.farewelltaxi.service
import com.example.farewelltaxi.dto.Admin.AdminRequestDto
import com.example.farewelltaxi.dto.Admin.AdminResponseDto
import com.example.farewelltaxi.enum.Role
import com.example.farewelltaxi.model.Admin
import com.example.farewelltaxi.model.User
import com.example.farewelltaxi.repository.AdminRepository
import com.example.farewelltaxi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class AdminService (val passwordEncoder: PasswordEncoder){

    @Value("\${app.security.special-key}")
    private val specialKey: String? = null

    @Autowired
    private val adminRepository: AdminRepository? = null

    @Autowired
    private val userRepository: UserRepository? = null

    fun createAdmin(adminRequestDto: AdminRequestDto, secretKey: String): AdminResponseDto {
        // 특정 키를 검증하는 로직
        require(this.specialKey == secretKey) { "Invalid key" }

        // User 엔티티 생성
        val user = User(
            email = adminRequestDto.email,
            password = passwordEncoder.encode(adminRequestDto.password), // passwordEncoder는 비밀번호를 암호화하는 데 사용됩니다.
            name = adminRequestDto.name,
            role = Role.ADMIN // Role은 열거형(enum)으로, ADMIN 권한을 부여합니다.
        )
        userRepository!!.save(user) // 생성된 User 엔티티를 저장
        // Admin 엔티티 생성
        val admin = Admin(user = user)
        adminRepository!!.save(admin)
        return  AdminResponseDto(email = admin.user.email, name = admin.user.name, password = admin.user.password)// 생성된 Admin 엔티티를 저장
    }

}